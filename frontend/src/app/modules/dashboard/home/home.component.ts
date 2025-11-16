import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule],
  template: `
    <div class="home-container">
      <h1>Bienvenido al Sistema Yguasu</h1>
      <p class="subtitle">Sistema de Gestión de Servicios Públicos</p>

      <div class="cards-grid">
        <mat-card class="stat-card">
          <mat-card-header>
            <mat-icon class="card-icon">people</mat-icon>
          </mat-card-header>
          <mat-card-content>
            <h2>Clientes</h2>
            <p class="stat-number">-</p>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card">
          <mat-card-header>
            <mat-icon class="card-icon">receipt</mat-icon>
          </mat-card-header>
          <mat-card-content>
            <h2>Facturas</h2>
            <p class="stat-number">-</p>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card">
          <mat-card-header>
            <mat-icon class="card-icon">speed</mat-icon>
          </mat-card-header>
          <mat-card-content>
            <h2>Lecturas</h2>
            <p class="stat-number">-</p>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card">
          <mat-card-header>
            <mat-icon class="card-icon">report_problem</mat-icon>
          </mat-card-header>
          <mat-card-content>
            <h2>Reclamos</h2>
            <p class="stat-number">-</p>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .home-container {
      padding: 20px;
    }

    h1 {
      font-size: 32px;
      margin-bottom: 8px;
      color: #333;
    }

    .subtitle {
      font-size: 16px;
      color: #666;
      margin-bottom: 40px;
    }

    .cards-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
    }

    .stat-card {
      text-align: center;
      padding: 20px;
      transition: transform 0.3s ease;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 4px 8px rgba(0,0,0,0.2);
      }

      mat-card-header {
        display: flex;
        justify-content: center;
        margin-bottom: 16px;

        .card-icon {
          font-size: 48px;
          width: 48px;
          height: 48px;
          color: #3f51b5;
        }
      }

      mat-card-content {
        h2 {
          font-size: 18px;
          margin-bottom: 8px;
          color: #666;
        }

        .stat-number {
          font-size: 36px;
          font-weight: 600;
          color: #333;
          margin: 0;
        }
      }
    }
  `]
})
export class HomeComponent {}
