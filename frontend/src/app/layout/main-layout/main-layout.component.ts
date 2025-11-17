import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDividerModule } from '@angular/material/divider';

/**
 * Interface para items del menú
 */
interface MenuItem {
  label: string;
  icon: string;
  route: string;
  children?: MenuItem[];
}

/**
 * Layout principal de la aplicación
 */
@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    MatDividerModule
  ],
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent {
  sidenavOpened = signal(true);

  menuItems: MenuItem[] = [
    {
      label: 'Dashboard',
      icon: 'dashboard',
      route: '/dashboard'
    },
    {
      label: 'Usuarios e Instalaciones',
      icon: 'people',
      route: '/usuarios-instalaciones'
    },
    {
      label: 'Reclamos',
      icon: 'report_problem',
      route: '/reclamos'
    },
    {
      label: 'Instalaciones',
      icon: 'home_work',
      route: '/instalaciones'
    },
    {
      label: 'Altas y Bajas',
      icon: 'swap_vert',
      route: '/altas-bajas'
    },
    {
      label: 'Cortes y Reconexiones',
      icon: 'power_settings_new',
      route: '/cortes-reconexiones',
      children: [
        {
          label: 'Gestión Individual',
          icon: 'person',
          route: '/cortes-reconexiones/gestion'
        },
        {
          label: 'Corte Masivo',
          icon: 'content_cut',
          route: '/cortes-reconexiones/corte-masivo'
        }
      ]
    }
  ];

  expandedItems = signal<Set<string>>(new Set());

  constructor(private router: Router) {}

  /**
   * Toggle del sidenav
   */
  toggleSidenav(): void {
    this.sidenavOpened.update(value => !value);
  }

  /**
   * Navega a una ruta
   */
  navigate(route: string): void {
    this.router.navigate([route]);
  }

  /**
   * Toggle de item expandido
   */
  toggleExpanded(label: string): void {
    const expanded = this.expandedItems();
    if (expanded.has(label)) {
      expanded.delete(label);
    } else {
      expanded.add(label);
    }
    this.expandedItems.set(new Set(expanded));
  }

  /**
   * Verifica si un item está expandido
   */
  isExpanded(label: string): boolean {
    return this.expandedItems().has(label);
  }

  /**
   * Verifica si la ruta está activa
   */
  isActive(route: string): boolean {
    return this.router.url === route;
  }
}
