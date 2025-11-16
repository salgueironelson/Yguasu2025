import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { InstalacionService } from '../../../../core/services/instalacion.service';
import { CortesReconexionesService } from '../../../../core/services/cortes-reconexiones.service';
import { InstalacionData } from '../../../../shared/models/instalacion.model';
import { Corte, Reconexion } from '../../../../shared/models/cortes-reconexiones.model';
import { CorteDialogComponent } from '../../dialogs/corte-dialog/corte-dialog.component';
import { ReconexionDialogComponent } from '../../dialogs/reconexion-dialog/reconexion-dialog.component';
import { HistorialDialogComponent } from '../../dialogs/historial-dialog/historial-dialog.component';

/**
 * Componente principal para gestión de cortes y reconexiones
 */
@Component({
  selector: 'app-gestion-cortes-reconexiones',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatTableModule,
    MatTabsModule,
    MatDialogModule
  ],
  templateUrl: './gestion-cortes-reconexiones.component.html',
  styleUrls: ['./gestion-cortes-reconexiones.component.scss']
})
export class GestionCortesReconexionesComponent implements OnInit {
  searchForm: FormGroup;
  searchLoading = signal(false);
  searchError = signal<string | null>(null);
  instalacionData = signal<InstalacionData | null>(null);

  // Listas
  cortes = signal<Corte[]>([]);
  reconexiones = signal<Reconexion[]>([]);
  loadingListas = signal(false);

  // Columnas de las tablas
  cortesColumns = ['codigoInstalacion', 'nombreCliente', 'fechaCorte', 'motivo', 'usuarioRegistro'];
  reconexionesColumns = ['codigoInstalacion', 'nombreCliente', 'fechaReconexion', 'motivo', 'usuarioRegistro'];

  constructor(
    private fb: FormBuilder,
    private instalacionService: InstalacionService,
    private cortesReconexionesService: CortesReconexionesService,
    private dialog: MatDialog
  ) {
    this.searchForm = this.fb.group({
      codigoInstalacion: ['', [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.cargarListas();
  }

  /**
   * Busca una instalación por código
   */
  onBuscar(): void {
    if (this.searchForm.invalid) {
      this.searchForm.markAllAsTouched();
      return;
    }

    this.searchLoading.set(true);
    this.searchError.set(null);
    this.instalacionData.set(null);

    const codigo = this.searchForm.value.codigoInstalacion;

    this.instalacionService.buscarPorCodigo(codigo).subscribe({
      next: (response) => {
        this.searchLoading.set(false);
        if (response.success && response.data) {
          this.instalacionData.set(response.data);
        } else {
          this.searchError.set('Instalación no encontrada');
        }
      },
      error: (error) => {
        this.searchLoading.set(false);
        this.searchError.set(
          error.error?.message || 'Error al buscar la instalación'
        );
      }
    });
  }

  /**
   * Abre el diálogo para cortar servicio
   */
  onCortarServicio(): void {
    const instalacion = this.instalacionData();
    if (!instalacion) return;

    const dialogRef = this.dialog.open(CorteDialogComponent, {
      width: '600px',
      data: {
        codigoInstalacion: instalacion.codigoInstalacion,
        nombreCliente: instalacion.nombreCompleto
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Recargar los datos de la instalación para actualizar el estado
        this.onBuscar();
        this.cargarListas();
      }
    });
  }

  /**
   * Abre el diálogo para reconectar servicio
   */
  onReconectarServicio(): void {
    const instalacion = this.instalacionData();
    if (!instalacion) return;

    const dialogRef = this.dialog.open(ReconexionDialogComponent, {
      width: '600px',
      data: {
        codigoInstalacion: instalacion.codigoInstalacion,
        nombreCliente: instalacion.nombreCompleto
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Recargar los datos de la instalación para actualizar el estado
        this.onBuscar();
        this.cargarListas();
      }
    });
  }

  /**
   * Abre el diálogo de historial
   */
  onVerHistorial(): void {
    const instalacion = this.instalacionData();
    if (!instalacion) return;

    this.dialog.open(HistorialDialogComponent, {
      width: '900px',
      maxWidth: '95vw',
      data: {
        codigoInstalacion: instalacion.codigoInstalacion
      }
    });
  }

  /**
   * Carga las listas de cortes y reconexiones
   */
  private cargarListas(): void {
    this.loadingListas.set(true);

    this.cortesReconexionesService.listarCortes().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.cortes.set(response.data);
        }
      }
    });

    this.cortesReconexionesService.listarReconexiones().subscribe({
      next: (response) => {
        this.loadingListas.set(false);
        if (response.success && response.data) {
          this.reconexiones.set(response.data);
        }
      }
    });
  }

  /**
   * Obtiene el badge de estado
   */
  getEstadoBadge(estado: string): { texto: string; clase: string } {
    switch (estado) {
      case 'A':
        return { texto: 'Activo', clase: 'estado-activo' };
      case 'C':
        return { texto: 'Cortado', clase: 'estado-cortado' };
      case 'I':
        return { texto: 'Inactivo', clase: 'estado-inactivo' };
      default:
        return { texto: 'Desconocido', clase: 'estado-desconocido' };
    }
  }

  /**
   * Verifica si se puede cortar el servicio
   */
  puedeCortarServicio(): boolean {
    const instalacion = this.instalacionData();
    return instalacion?.estado === 'A';
  }

  /**
   * Verifica si se puede reconectar el servicio
   */
  puedeReconectarServicio(): boolean {
    const instalacion = this.instalacionData();
    return instalacion?.estado === 'C';
  }

  /**
   * Limpia el formulario de búsqueda
   */
  onLimpiar(): void {
    this.searchForm.reset();
    this.instalacionData.set(null);
    this.searchError.set(null);
  }
}
