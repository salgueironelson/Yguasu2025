import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '@core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  authService = inject(AuthService);

  menuItems = [
    { path: '/dashboard/home', icon: 'home', label: 'Inicio' },
    { path: '/dashboard/clientes', icon: 'people', label: 'Clientes' },
    { path: '/dashboard/catastro', icon: 'map', label: 'Catastro' },
    { path: '/dashboard/servicios', icon: 'build', label: 'Servicios' },
    { path: '/dashboard/facturacion', icon: 'receipt', label: 'Facturación' },
    { path: '/dashboard/lecturas', icon: 'speed', label: 'Lecturas' },
    { path: '/dashboard/operaciones', icon: 'settings', label: 'Operaciones' },
    { path: '/dashboard/reclamos', icon: 'report_problem', label: 'Reclamos' }
  ];

  logout(): void {
    this.authService.logout();
  }
}
