import { Routes } from '@angular/router';
import { PortfolioComponent } from './portfolio/portfolio.component';
import { RetirementComponent } from './retirement/retirement.component';

export const routes: Routes = [
  { path: 'portfolio', component: PortfolioComponent },
  { path: 'retirement', component: RetirementComponent },
  { path: '', redirectTo: '/portfolio', pathMatch: 'full' }
];
