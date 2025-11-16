import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatMenuModule } from '@angular/material/menu';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ReclamoService } from '@core/services/reclamo.service';
import { Reclamo, TIPOS_RECLAMO, ESTADOS_RECLAMO } from '@shared/models/reclamo.model';
import { ComentarioDialogComponent } from '../dialogs/comentario-dialog/comentario-dialog.component';
import { TransferenciaDialogComponent } from '../dialogs/transferencia-dialog/transferencia-dialog.component';
import { ConclusionDialogComponent } from '../dialogs/conclusion-dialog/conclusion-dialog.component';

@Component({
  selector: 'app-reclamos-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatChipsModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    MatMenuModule,
    MatSnackBarModule
  ],
  templateUrl: './reclamos-list.component.html',
  styleUrls: ['./reclamos-list.component.scss']
})
export class ReclamosListComponent implements OnInit {
  private fb = inject(FormBuilder);
  private reclamoService = inject(ReclamoService);
  private router = inject(Router);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  // Datos
  reclamos = signal<Reclamo[]>([]);
  loading = signal(false);

  // Paginación
  totalElements = signal(0);
  pageSize = signal(10);
  pageIndex = signal(0);

  // Filtros
  filtrosForm: FormGroup;
  mostrarFiltros = signal(false);
  tiposReclamo = TIPOS_RECLAMO;
  estadosReclamo = ESTADOS_RECLAMO;

  // Tabla
  displayedColumns: string[] = [
    'numero',
    'codigoInstalacion',
    'reclamante',
    'tipoReclamo',
    'fechaReclamo',
    'estado',
    'procedente',
    'acciones'
  ];

  constructor() {
    this.filtrosForm = this.fb.group({
      busqueda: [''],
      tipoReclamo: [''],
      estado: [''],
      departamento: [''],
      procedente: [''],
      fechaDesde: [null],
      fechaHasta: [null]
    });
  }

  ngOnInit(): void {
    this.cargarReclamos();
  }

  cargarReclamos(): void {
    this.loading.set(true);

    const filtros = this.filtrosForm.value;

    this.reclamoService.listar(filtros, this.pageIndex(), this.pageSize()).subscribe({
      next: (response) => {
        this.loading.set(false);
        if (response.success && response.data) {
          this.reclamos.set(response.data.content);
          this.totalElements.set(response.data.totalElements);
        }
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  aplicarFiltros(): void {
    this.pageIndex.set(0);
    this.cargarReclamos();
  }

  limpiarFiltros(): void {
    this.filtrosForm.reset();
    this.pageIndex.set(0);
    this.cargarReclamos();
  }

  toggleFiltros(): void {
    this.mostrarFiltros.set(!this.mostrarFiltros());
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.cargarReclamos();
  }

  crearNuevo(): void {
    this.router.navigate(['/dashboard/reclamos/crear']);
  }

  verDetalle(reclamo: Reclamo): void {
    this.router.navigate(['/dashboard/reclamos', reclamo.id]);
  }

  agregarComentario(reclamo: Reclamo): void {
    const dialogRef = this.dialog.open(ComentarioDialogComponent, {
      width: '600px',
      data: {
        reclamoId: reclamo.id,
        reclamoNumero: reclamo.numero
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && reclamo.id) {
        this.reclamoService.agregarComentario(reclamo.id, result).subscribe({
          next: (response) => {
            if (response.success) {
              this.snackBar.open('Comentario agregado exitosamente', 'Cerrar', {
                duration: 3000,
                horizontalPosition: 'end',
                verticalPosition: 'top'
              });
              this.cargarReclamos();
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

  transferir(reclamo: Reclamo): void {
    const dialogRef = this.dialog.open(TransferenciaDialogComponent, {
      width: '600px',
      data: {
        reclamoId: reclamo.id,
        reclamoNumero: reclamo.numero
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && reclamo.id) {
        this.reclamoService.transferir(reclamo.id, result).subscribe({
          next: (response) => {
            if (response.success) {
              this.snackBar.open('Reclamo transferido exitosamente', 'Cerrar', {
                duration: 3000,
                horizontalPosition: 'end',
                verticalPosition: 'top'
              });
              this.cargarReclamos();
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

  concluir(reclamo: Reclamo): void {
    const dialogRef = this.dialog.open(ConclusionDialogComponent, {
      width: '650px',
      data: {
        reclamoId: reclamo.id,
        reclamoNumero: reclamo.numero
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && reclamo.id) {
        this.reclamoService.concluir(reclamo.id, result).subscribe({
          next: (response) => {
            if (response.success) {
              this.snackBar.open('Reclamo concluido exitosamente', 'Cerrar', {
                duration: 3000,
                horizontalPosition: 'end',
                verticalPosition: 'top'
              });
              this.cargarReclamos();
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

  imprimirFicha(reclamo: Reclamo): void {
    // TODO: Generar PDF de ficha de reclamo
    console.log('Imprimir ficha:', reclamo);
  }

  imprimirFichaTrabajo(reclamo: Reclamo): void {
    // TODO: Generar PDF de ficha de trabajo
    console.log('Imprimir ficha de trabajo:', reclamo);
  }

  getEstadoColor(estado?: string): string {
    switch (estado) {
      case 'PENDIENTE':
        return 'warn';
      case 'EN_PROCESO':
        return 'primary';
      case 'TRANSFERIDO':
        return 'accent';
      case 'CONCLUIDO':
        return 'success';
      default:
        return 'default';
    }
  }

  getEstadoLabel(estado?: string): string {
    const estadoObj = this.estadosReclamo.find(e => e.value === estado);
    return estadoObj?.label || estado || '-';
  }

  getTipoReclamoLabel(tipo?: string): string {
    const tipoObj = this.tiposReclamo.find(t => t.value === tipo);
    return tipoObj?.label || tipo || '-';
  }

  formatFecha(fecha?: Date): string {
    if (!fecha) return '-';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-BO', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    });
  }

  puedeAgregarComentario(reclamo: Reclamo): boolean {
    // TODO: Verificar si el usuario actual es el usuario asignado
    return reclamo.estado !== 'CONCLUIDO';
  }

  puedeTransferir(reclamo: Reclamo): boolean {
    return reclamo.estado !== 'CONCLUIDO';
  }

  puedeConcluir(reclamo: Reclamo): boolean {
    return reclamo.estado !== 'CONCLUIDO';
  }
}
