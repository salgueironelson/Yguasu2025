import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SelectionModel } from '@angular/cdk/collections';
import { CortesReconexionesService } from '../../../../core/services/cortes-reconexiones.service';
import {
  Plomero,
  InstalacionConDeuda,
  MOTIVOS_CORTE
} from '../../../../shared/models/cortes-reconexiones.model';

/**
 * Componente para corte masivo de servicios
 */
@Component({
  selector: 'app-corte-masivo',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatCheckboxModule,
    MatDialogModule,
    MatSnackBarModule
  ],
  templateUrl: './corte-masivo.component.html',
  styleUrls: ['./corte-masivo.component.scss']
})
export class CorteMasivoComponent implements OnInit {
  filtrosForm: FormGroup;
  corteForm: FormGroup;

  plomeros = signal<Plomero[]>([]);
  instalaciones = signal<InstalacionConDeuda[]>([]);
  selection = new SelectionModel<InstalacionConDeuda>(true, []);

  loadingPlomeros = signal(false);
  loadingInstalaciones = signal(false);
  procesandoCorte = signal(false);

  errorMessage = signal<string | null>(null);
  motivos = MOTIVOS_CORTE;

  // Columnas de la tabla
  displayedColumns = [
    'select',
    'codigoInstalacion',
    'nombreCliente',
    'direccion',
    'zona',
    'cantidadFacturasAdeudadas',
    'montoDeuda'
  ];

  constructor(
    private fb: FormBuilder,
    private cortesReconexionesService: CortesReconexionesService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {
    this.filtrosForm = this.fb.group({
      zonaInicial: ['', [Validators.required]],
      zonaFinal: ['', [Validators.required]],
      cantidadFacturas: [1, [Validators.required, Validators.min(1)]]
    });

    this.corteForm = this.fb.group({
      idPlomero: ['', [Validators.required]],
      motivo: ['Falta de pago', [Validators.required]],
      observaciones: ['']
    });
  }

  ngOnInit(): void {
    this.cargarPlomeros();
  }

  /**
   * Carga la lista de plomeros activos
   */
  private cargarPlomeros(): void {
    this.loadingPlomeros.set(true);

    this.cortesReconexionesService.listarPlomerosActivos().subscribe({
      next: (response) => {
        this.loadingPlomeros.set(false);
        if (response.success && response.data) {
          this.plomeros.set(response.data);
        }
      },
      error: (error) => {
        this.loadingPlomeros.set(false);
        this.mostrarError('Error al cargar plomeros');
      }
    });
  }

  /**
   * Busca instalaciones con deuda según los filtros
   */
  onBuscarInstalaciones(): void {
    if (this.filtrosForm.invalid) {
      this.filtrosForm.markAllAsTouched();
      return;
    }

    this.loadingInstalaciones.set(true);
    this.errorMessage.set(null);
    this.instalaciones.set([]);
    this.selection.clear();

    const filtros = this.filtrosForm.value;

    this.cortesReconexionesService.buscarInstalacionesConDeuda(filtros).subscribe({
      next: (response) => {
        this.loadingInstalaciones.set(false);
        if (response.success && response.data) {
          this.instalaciones.set(response.data);
          if (response.data.length === 0) {
            this.mostrarInfo('No se encontraron instalaciones con los criterios especificados');
          } else {
            this.mostrarExito(`Se encontraron ${response.data.length} instalaciones`);
          }
        } else {
          this.errorMessage.set(response.message || 'No se encontraron instalaciones');
        }
      },
      error: (error) => {
        this.loadingInstalaciones.set(false);
        this.errorMessage.set(
          error.error?.message || 'Error al buscar instalaciones'
        );
      }
    });
  }

  /**
   * Procesa el corte masivo
   */
  onProcesarCorte(): void {
    if (this.corteForm.invalid) {
      this.corteForm.markAllAsTouched();
      return;
    }

    if (this.selection.selected.length === 0) {
      this.mostrarError('Debe seleccionar al menos una instalación');
      return;
    }

    // Confirmar antes de procesar
    if (!confirm(`¿Está seguro que desea cortar ${this.selection.selected.length} instalaciones?`)) {
      return;
    }

    this.procesandoCorte.set(true);

    const request = {
      idPlomero: this.corteForm.value.idPlomero,
      idsInstalaciones: this.selection.selected.map(i => i.idInstalacion),
      motivo: this.corteForm.value.motivo,
      observaciones: this.corteForm.value.observaciones
    };

    this.cortesReconexionesService.corteMasivo(request).subscribe({
      next: (response) => {
        this.procesandoCorte.set(false);
        if (response.success && response.data) {
          const resultado = response.data;
          this.mostrarResultado(resultado.totalExitosos, resultado.totalFallidos, resultado.errores);

          // Limpiar selección y recargar búsqueda
          this.selection.clear();
          this.onBuscarInstalaciones();
        } else {
          this.mostrarError(response.message || 'Error al procesar corte masivo');
        }
      },
      error: (error) => {
        this.procesandoCorte.set(false);
        this.mostrarError(
          error.error?.message || 'Error al procesar corte masivo'
        );
      }
    });
  }

  /**
   * Verifica si todas las filas están seleccionadas
   */
  isAllSelected(): boolean {
    const numSelected = this.selection.selected.length;
    const numRows = this.instalaciones().length;
    return numSelected === numRows;
  }

  /**
   * Selecciona o deselecciona todas las filas
   */
  toggleAllRows(): void {
    if (this.isAllSelected()) {
      this.selection.clear();
    } else {
      this.instalaciones().forEach(row => this.selection.select(row));
    }
  }

  /**
   * Obtiene el label del checkbox principal
   */
  checkboxLabel(row?: InstalacionConDeuda): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deseleccionar' : 'seleccionar'} todos`;
    }
    return `${this.selection.isSelected(row) ? 'deseleccionar' : 'seleccionar'} fila ${row.codigoInstalacion}`;
  }

  /**
   * Limpia los filtros
   */
  onLimpiar(): void {
    this.filtrosForm.reset({
      cantidadFacturas: 1
    });
    this.instalaciones.set([]);
    this.selection.clear();
    this.errorMessage.set(null);
  }

  /**
   * Muestra mensaje de éxito
   */
  private mostrarExito(mensaje: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['snackbar-success']
    });
  }

  /**
   * Muestra mensaje de error
   */
  private mostrarError(mensaje: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 5000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['snackbar-error']
    });
  }

  /**
   * Muestra mensaje de info
   */
  private mostrarInfo(mensaje: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['snackbar-info']
    });
  }

  /**
   * Muestra el resultado del corte masivo
   */
  private mostrarResultado(exitosos: number, fallidos: number, errores: string[]): void {
    let mensaje = `Corte masivo completado.\nExitosos: ${exitosos}, Fallidos: ${fallidos}`;

    if (errores.length > 0) {
      mensaje += '\n\nErrores:\n' + errores.slice(0, 5).join('\n');
      if (errores.length > 5) {
        mensaje += `\n...y ${errores.length - 5} más`;
      }
    }

    alert(mensaje);
  }
}
