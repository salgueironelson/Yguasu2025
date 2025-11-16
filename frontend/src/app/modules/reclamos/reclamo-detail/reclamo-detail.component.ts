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
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ReclamoService } from '@core/services/reclamo.service';
import { Reclamo, Paso } from '@shared/models/reclamo.model';
import { ComentarioDialogComponent } from '../dialogs/comentario-dialog/comentario-dialog.component';
import { TransferenciaDialogComponent } from '../dialogs/transferencia-dialog/transferencia-dialog.component';
import { ConclusionDialogComponent } from '../dialogs/conclusion-dialog/conclusion-dialog.component';

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
    MatListModule,
    MatSnackBarModule
  ],
  templateUrl: './reclamo-detail.component.html',
  styleUrls: ['./reclamo-detail.component.scss']
})
export class ReclamoDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private reclamoService = inject(ReclamoService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

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
    const reclamoActual = this.reclamo();
    if (!reclamoActual?.id) return;

    const dialogRef = this.dialog.open(ComentarioDialogComponent, {
      width: '600px',
      data: {
        reclamoId: reclamoActual.id,
        reclamoNumero: reclamoActual.numero
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && reclamoActual.id) {
        this.reclamoService.agregarComentario(reclamoActual.id, result).subscribe({
          next: (response) => {
            if (response.success) {
              this.snackBar.open('Comentario agregado exitosamente', 'Cerrar', {
                duration: 3000,
                horizontalPosition: 'end',
                verticalPosition: 'top'
              });
              this.cargarReclamo();
              this.cargarHistorial();
            }
          },
          error: () => {
            this.snackBar.open('Error al agregar comentario', 'Cerrar', {
              duration: 3000,
              horizontalPosition: 'end',
              verticalPosition: 'top'
            });
          }
        });
      }
    });
  }

  transferir(): void {
    const reclamoActual = this.reclamo();
    if (!reclamoActual?.id) return;

    const dialogRef = this.dialog.open(TransferenciaDialogComponent, {
      width: '600px',
      data: {
        reclamoId: reclamoActual.id,
        reclamoNumero: reclamoActual.numero
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && reclamoActual.id) {
        this.reclamoService.transferir(reclamoActual.id, result).subscribe({
          next: (response) => {
            if (response.success) {
              this.snackBar.open('Reclamo transferido exitosamente', 'Cerrar', {
                duration: 3000,
                horizontalPosition: 'end',
                verticalPosition: 'top'
              });
              this.cargarReclamo();
              this.cargarHistorial();
            }
          },
          error: () => {
            this.snackBar.open('Error al transferir reclamo', 'Cerrar', {
              duration: 3000,
              horizontalPosition: 'end',
              verticalPosition: 'top'
            });
          }
        });
      }
    });
  }

  concluir(): void {
    const reclamoActual = this.reclamo();
    if (!reclamoActual?.id) return;

    const dialogRef = this.dialog.open(ConclusionDialogComponent, {
      width: '650px',
      data: {
        reclamoId: reclamoActual.id,
        reclamoNumero: reclamoActual.numero
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && reclamoActual.id) {
        this.reclamoService.concluir(reclamoActual.id, result).subscribe({
          next: (response) => {
            if (response.success) {
              this.snackBar.open('Reclamo concluido exitosamente', 'Cerrar', {
                duration: 3000,
                horizontalPosition: 'end',
                verticalPosition: 'top'
              });
              this.cargarReclamo();
              this.cargarHistorial();
            }
          },
          error: () => {
            this.snackBar.open('Error al concluir reclamo', 'Cerrar', {
              duration: 3000,
              horizontalPosition: 'end',
              verticalPosition: 'top'
            });
          }
        });
      }
    });
  }

  imprimirFicha(): void {
    console.log('Imprimir ficha');
  }

  imprimirFichaTrabajo(): void {
    console.log('Imprimir ficha de trabajo');
  }

  getEstadoClass(estado?: string): string {
    return `estado-${ (estado || 'default').toLowerCase()}`;
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
