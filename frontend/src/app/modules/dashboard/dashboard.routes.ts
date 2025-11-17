import { Routes } from '@angular/router';

export const DASHBOARD_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('../../layout/main-layout/main-layout.component').then(m => m.MainLayoutComponent),
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () => import('./dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'reclamos',
        loadChildren: () => import('../reclamos/reclamos.routes').then(m => m.RECLAMOS_ROUTES)
      },
      {
        path: 'altas-bajas',
        loadComponent: () => import('../altas-bajas/components/gestion-altas-bajas/gestion-altas-bajas.component').then(m => m.GestionAltasBajasComponent)
      },
      {
        path: 'cortes-reconexiones',
        children: [
          {
            path: '',
            redirectTo: 'gestion',
            pathMatch: 'full'
          },
          {
            path: 'gestion',
            loadComponent: () => import('../cortes-reconexiones/components/gestion-cortes-reconexiones/gestion-cortes-reconexiones.component').then(m => m.GestionCortesReconexionesComponent)
          },
          {
            path: 'corte-masivo',
            loadComponent: () => import('../cortes-reconexiones/components/corte-masivo/corte-masivo.component').then(m => m.CorteMasivoComponent)
          }
        ]
      },
      {
        path: 'usuarios-instalaciones',
        loadComponent: () => import('../usuarios-instalaciones/components/gestion-usuarios/gestion-usuarios.component').then(m => m.GestionUsuariosComponent)
      }
    ]
  }
];
