import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

/**
 * Interface para estadísticas del dashboard
 */
interface DashboardStats {
  totalInstalaciones: number;
  instalacionesActivas: number;
  instalacionesCortadas: number;
  instalacionesInactivas: number;
  totalReclamos: number;
  reclamosPendientes: number;
  totalCortes: number;
  totalReconexiones: number;
}

/**
 * Interface para tarjeta de estadística
 */
interface StatCard {
  title: string;
  value: number;
  icon: string;
  color: string;
  route?: string;
  description?: string;
}

/**
 * Componente Dashboard principal
 */
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatGridListModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  loading = signal(true);
  stats = signal<DashboardStats>({
    totalInstalaciones: 0,
    instalacionesActivas: 0,
    instalacionesCortadas: 0,
    instalacionesInactivas: 0,
    totalReclamos: 0,
    reclamosPendientes: 0,
    totalCortes: 0,
    totalReconexiones: 0
  });

  statCards = signal<StatCard[]>([]);

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  /**
   * Carga las estadísticas del sistema
   */
  private cargarEstadisticas(): void {
    // TODO: Llamar a servicios reales para obtener estadísticas
    // Por ahora usamos datos de ejemplo
    setTimeout(() => {
      const mockStats: DashboardStats = {
        totalInstalaciones: 1250,
        instalacionesActivas: 980,
        instalacionesCortadas: 150,
        instalacionesInactivas: 120,
        totalReclamos: 342,
        reclamosPendientes: 45,
        totalCortes: 215,
        totalReconexiones: 189
      };

      this.stats.set(mockStats);
      this.generarTarjetas(mockStats);
      this.loading.set(false);
    }, 1000);
  }

  /**
   * Genera las tarjetas de estadísticas
   */
  private generarTarjetas(stats: DashboardStats): void {
    const cards: StatCard[] = [
      {
        title: 'Total Instalaciones',
        value: stats.totalInstalaciones,
        icon: 'home_work',
        color: '#1976d2',
        route: '/instalaciones',
        description: 'Instalaciones registradas'
      },
      {
        title: 'Instalaciones Activas',
        value: stats.instalacionesActivas,
        icon: 'check_circle',
        color: '#4caf50',
        route: '/instalaciones',
        description: 'En servicio activo'
      },
      {
        title: 'Instalaciones Cortadas',
        value: stats.instalacionesCortadas,
        icon: 'power_off',
        color: '#f44336',
        route: '/cortes-reconexiones',
        description: 'Requieren atención'
      },
      {
        title: 'Instalaciones Inactivas',
        value: stats.instalacionesInactivas,
        icon: 'cancel',
        color: '#9e9e9e',
        route: '/instalaciones',
        description: 'Dadas de baja'
      },
      {
        title: 'Total Reclamos',
        value: stats.totalReclamos,
        icon: 'report_problem',
        color: '#ff9800',
        route: '/reclamos',
        description: 'Reclamos registrados'
      },
      {
        title: 'Reclamos Pendientes',
        value: stats.reclamosPendientes,
        icon: 'pending',
        color: '#ff5722',
        route: '/reclamos',
        description: 'Requieren resolución'
      },
      {
        title: 'Cortes Realizados',
        value: stats.totalCortes,
        icon: 'content_cut',
        color: '#e91e63',
        route: '/cortes-reconexiones',
        description: 'Histórico de cortes'
      },
      {
        title: 'Reconexiones',
        value: stats.totalReconexiones,
        icon: 'power',
        color: '#00bcd4',
        route: '/cortes-reconexiones',
        description: 'Servicios reconectados'
      }
    ];

    this.statCards.set(cards);
  }

  /**
   * Navega a una ruta específica
   */
  navigateTo(route?: string): void {
    if (route) {
      this.router.navigate([route]);
    }
  }

  /**
   * Calcula el porcentaje de instalaciones activas
   */
  get porcentajeActivas(): number {
    const stats = this.stats();
    if (stats.totalInstalaciones === 0) return 0;
    return Math.round((stats.instalacionesActivas / stats.totalInstalaciones) * 100);
  }

  /**
   * Calcula el porcentaje de instalaciones cortadas
   */
  get porcentajeCortadas(): number {
    const stats = this.stats();
    if (stats.totalInstalaciones === 0) return 0;
    return Math.round((stats.instalacionesCortadas / stats.totalInstalaciones) * 100);
  }

  /**
   * Calcula el porcentaje de reclamos pendientes
   */
  get porcentajeReclamosPendientes(): number {
    const stats = this.stats();
    if (stats.totalReclamos === 0) return 0;
    return Math.round((stats.reclamosPendientes / stats.totalReclamos) * 100);
  }
}
