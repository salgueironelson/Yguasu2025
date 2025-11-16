import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatListModule } from '@angular/material/list';

import { ReclamoService } from '@core/services/reclamo.service';
import { Reclamo, Paso } from '@shared/models/reclamo.model';

@Component({
  selector: 'app-reclamo-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatExpansionModule,
    MatListModule
  ],
  templateUrl: './reclamo-detail.component.html',
  styleUrls: ['./reclamo-detail.component.scss']
})
export class ReclamoDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private reclamoService = inject(ReclamoService);

  reclamo = signal<Reclamo | null>(null);
  historial = signal<Paso[]>([]);
  loading = signal(false);
  loadingHistorial = signal(false);
  idReclamo: number = 0;

  ngOnInit(): void {
    this.idReclamo = Number(this.route.snapshot.paramMap.get('id'));
    if (this.idReclamo) {
      this.cargarReclamo();
      this.cargarHistorial();
    }
  }

  cargarReclamo(): void {
    this.loading.set(true);

    this.reclamoService.obtenerPorId(this.idReclamo).subscribe({
      next: (response) => {
        this.loading.set(false);
        if (response.success && response.data) {
          this.reclamo.set(response.data);
        }
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  cargarHistorial(): void {
    this.loadingHistorial.set(true);

    this.reclamoService.obtenerHistorial(this.idReclamo).subscribe({
      next: (response) => {
        this.loadingHistorial.set(false);
        if (response.success && response.data) {
          this.historial.set(response.data);
        }
      },
      error: () => {
        this.loadingHistorial.set(false);
      }
    });
  }

  volver(): void {
    this.router.navigate(['/dashboard/reclamos']);
  }

  agregarComentario(): void {
    // TODO: Abrir diálogo de comentario
    console.log('Agregar comentario');
  }

  transferir(): void {
    // TODO: Abrir diálogo de transferencia
    console.log('Transferir');
  }

  concluir(): void {
    // TODO: Abrir diálogo de conclusión
    console.log('Concluir');
  }

  imprimirFicha(): void {
    // TODO: Generar PDF
    console.log('Imprimir ficha');
  }

  imprimirFichaTrabajo(): void {
    // TODO: Generar PDF
    console.log('Imprimir ficha de trabajo');
  }

  getEstadoClass(estado?: string): string {
    return `estado-${estado?.toLowerCase() || 'default'}`;
  }

  getTipoPasoIcon(tipoPaso?: string): string {
    switch (tipoPaso) {
      case 'CREACION':
        return 'add_circle';
      case 'COMENTARIO':
        return 'comment';
      case 'TRANSFERENCIA':
        return 'send';
      case 'CONCLUSION':
        return 'check_circle';
      default:
        return 'info';
    }
  }

  getTipoPasoColor(tipoPaso?: string): string {
    switch (tipoPaso) {
      case 'CREACION':
        return 'primary';
      case 'COMENTARIO':
        return 'accent';
      case 'TRANSFERENCIA':
        return 'warn';
      case 'CONCLUSION':
        return 'success';
      default:
        return '';
    }
  }

  formatFecha(fecha?: Date): string {
    if (!fecha) return '-';
    const date = new Date(fecha);
    return date.toLocaleString('es-BO', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  puedeAgregarComentario(): boolean {
    return this.reclamo()?.estado !== 'CONCLUIDO';
  }

  puedeTransferir(): boolean {
    return this.reclamo()?.estado !== 'CONCLUIDO';
  }

  puedeConcluir(): boolean {
    return this.reclamo()?.estado !== 'CONCLUIDO';
  }
}
