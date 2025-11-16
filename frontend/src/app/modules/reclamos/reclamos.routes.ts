import { Routes } from '@angular/router';

export const RECLAMOS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./reclamos-list/reclamos-list.component').then(m => m.ReclamosListComponent)
  },
  {
    path: 'crear',
    loadComponent: () => import('./reclamo-create/reclamo-create.component').then(m => m.ReclamoCreateComponent)
  },
  {
    path: ':id',
    loadComponent: () => import('./reclamo-detail/reclamo-detail.component').then(m => m.ReclamoDetailComponent)
  }
];
