import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { Subject, debounceTime, distinctUntilChanged, switchMap, of, catchError } from 'rxjs';

interface SearchResult {
  symbol: string;
  name: string;
  type: string;
  exchange: string;
}

interface Asset {
  name: string;
  symbol: string;
  value: number;
  category: string;
  currentPrice?: number;
  quantity?: number;
}

@Component({
  selector: 'app-portfolio',
  standalone: true,
  imports: [CommonModule, FormsModule, BaseChartDirective],
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.scss'
})
export class PortfolioComponent implements OnInit {
  assets: Asset[] = [];
  totalValue: number = 0;
  
  newSymbol: string = '';
  newQuantity: number = 1;
  isAdding: boolean = false;
  isResetting: boolean = false;
  isLoading: boolean = true;

  searchQuery$ = new Subject<string>();
  suggestions: SearchResult[] = [];
  showSuggestions = false;

  public pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: { display: true, position: 'right' }
    }
  };
  public pieChartData: ChartData<'pie', number[], string | string[]> = {
    labels: [],
    datasets: [{ data: [] }]
  };
  public pieChartType: ChartType = 'pie';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchAssets();
    this.setupSearch();
  }

  setupSearch(): void {
    this.searchQuery$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(query => {
        if (!query || query.length < 2) {
          return of([]);
        }
        return this.http.get<SearchResult[]>('http://34.228.247.3:8081/api/portfolio/search?q=' + query).pipe(
          catchError(() => of([]))
        );
      })
    ).subscribe(results => {
      this.suggestions = results;
      this.showSuggestions = results.length > 0;
    });
  }

  onSearchInput(value: string): void {
    this.searchQuery$.next(value);
  }

  selectSuggestion(symbol: string): void {
    this.newSymbol = symbol;
    this.showSuggestions = false;
  }

  fetchAssets(): void {
    this.isLoading = true;
    this.http.get<Asset[]>('http://34.228.247.3:8081/api/portfolio/assets').subscribe({
      next: (data) => {
        this.assets = data;
        this.totalValue = data.reduce((sum, asset) => sum + asset.value, 0);
        this.updateChart();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Failed to fetch assets', err);
        this.isLoading = false;
      }
    });
  }

  resetPortfolio(): void {
    if (!confirm('Are you sure you want to reset your entire portfolio?')) return;
    this.isResetting = true;
    this.http.delete('http://34.228.247.3:8081/api/portfolio/reset').subscribe({
      next: () => {
        this.fetchAssets();
        this.isResetting = false;
      },
      error: (err) => {
        console.error('Failed to reset portfolio', err);
        this.isResetting = false;
      }
    });
  }

  addAsset(): void {
    if (!this.newSymbol) return;
    this.isAdding = true;
    this.http.post<Asset>('http://34.228.247.3:8081/api/portfolio/add', {
      symbol: this.newSymbol,
      quantity: this.newQuantity
    }).subscribe({
      next: (asset) => {
        this.newSymbol = '';
        this.newQuantity = 1;
        this.isAdding = false;
        this.fetchAssets(); // Refresh list
      },
      error: (err) => {
        console.error('Failed to add asset', err);
        alert('Failed to add asset. Check symbol or try again later.');
        this.isAdding = false;
      }
    });
  }

  updateChart(): void {
    this.pieChartData = {
      labels: this.assets.map(a => a.symbol),
      datasets: [{
        data: this.assets.map(a => a.value),
        backgroundColor: ['#9b1c2c', '#333333', '#666666', '#a3a3a3'], // Vanguard inspired colors
      }]
    };
  }
}
