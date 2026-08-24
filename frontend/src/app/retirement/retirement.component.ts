import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatSliderModule } from '@angular/material/slider';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartType } from 'chart.js';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';

interface RetirementRequest {
  currentAge: number;
  retirementAge: number;
  currentSavings: number;
  monthlyContribution: number;
  expectedAnnualReturn: number;
}

interface YearlyProjection {
  age: number;
  balance: number;
}

interface RetirementResponse {
  totalProjectedSavings: number;
  projections: YearlyProjection[];
}

@Component({
  selector: 'app-retirement',
  standalone: true,
  imports: [CommonModule, FormsModule, MatSliderModule, BaseChartDirective],
  templateUrl: './retirement.component.html',
  styleUrl: './retirement.component.scss'
})
export class RetirementComponent implements OnInit {
  request: RetirementRequest = {
    currentAge: 30,
    retirementAge: 65,
    currentSavings: 50000,
    monthlyContribution: 1000,
    expectedAnnualReturn: 0.07
  };

  totalProjected: number = 0;
  private inputChange = new Subject<void>();

  public lineChartData: ChartConfiguration['data'] = {
    datasets: [{ data: [], label: 'Projected Balance', backgroundColor: 'rgba(155,28,44,0.2)', borderColor: '#9b1c2c', fill: true }],
    labels: []
  };
  public lineChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: { legend: { display: false } }
  };
  public lineChartType: ChartType = 'line';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.calculate();

    this.inputChange.pipe(
      debounceTime(300)
    ).subscribe(() => {
      this.calculate();
    });
  }

  onInputChange(): void {
    this.inputChange.next();
  }

  calculate(): void {
    this.http.post<RetirementResponse>('http://localhost:8081/api/retirement/calculate', this.request)
      .subscribe(res => {
        this.totalProjected = res.totalProjectedSavings;
        this.lineChartData = {
          labels: res.projections.map(p => p.age.toString()),
          datasets: [{
            data: res.projections.map(p => p.balance),
            label: 'Projected Balance',
            backgroundColor: 'rgba(155,28,44,0.2)',
            borderColor: '#9b1c2c',
            fill: true
          }]
        };
      });
  }
}
