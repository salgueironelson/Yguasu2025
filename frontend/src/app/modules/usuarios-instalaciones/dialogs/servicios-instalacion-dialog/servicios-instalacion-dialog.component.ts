import { Component, inject, signal, OnInit, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ServiciosInstalacionService } from '../../../../core/services/servicios-instalacion.service';
import {
  Servicio,
  InstalacionServicio,
  Medidor
} from '../../../../shared/models/servicio.model';

/**
 * Diálogo para gestionar servicios de una instalación
 */
@Component({
  selector: 'app-servicios-instalacion-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatTableModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './servicios-instalacion-dialog.component.html',
  styleUrls: ['./servicios-instalacion-dialog.component.scss']
})
export class ServiciosInstalacionDialogComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly serviciosService = inject(ServiciosInstalacionService);
  private readonly snackBar = inject(MatSnackBar);
  private readonly dialogRef = inject(MatDialogRef<ServiciosInstalacionDialogComponent>);

  serviciosAsignados = signal<InstalacionServicio[]>([]);
  serviciosDisponibles = signal<Servicio[]>([]);
  medidoresDisponibles = signal<Medidor[]>([]);
  loading = signal(false);
  saving = signal(false);

  asignarForm: FormGroup;
  displayedColumns = ['nombreServicio', 'serieMedidor', 'estado', 'acciones'];

  constructor(@Inject(MAT_DIALOG_DATA) public data: { idInstalacion: number, codigoInstalacion: number }) {
    this.asignarForm = this.fb.group({
      idServicio: ['', Validators.required],
      idMedidor: ['']
    });
  }

  ngOnInit(): void {
    this.cargarDatos();
  }

  /**
   * Carga todos los datos necesarios
   */
  cargarDatos(): void {
    this.loading.set(true);

    // Cargar servicios asignados
    this.serviciosService.listarServiciosPorInstalacion(this.data.idInstalacion).subscribe({
      next: (servicios) => {
        this.serviciosAsignados.set(servicios);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Error al cargar servicios asignados:', error);
        this.snackBar.open('Error al cargar servicios asignados', 'Cerrar', { duration: 3000 });
        this.loading.set(false);
      }
    });

    // Cargar servicios disponibles
    this.serviciosService.listarServiciosDisponibles().subscribe({
      next: (servicios) => this.serviciosDisponibles.set(servicios),
      error: (error) => console.error('Error al cargar servicios disponibles:', error)
    });

    // Cargar medidores disponibles
    this.serviciosService.listarMedidoresDisponibles().subscribe({
      next: (medidores) => this.medidoresDisponibles.set(medidores),
      error: (error) => console.error('Error al cargar medidores:', error)
    });
  }

  /**
   * Asigna un servicio a la instalación
   */
  asignarServicio(): void {
    if (this.asignarForm.invalid) {
      return;
    }

    this.saving.set(true);

    const dto = {
      idInstalacion: this.data.idInstalacion,
      idServicio: this.asignarForm.value.idServicio,
      idMedidor: this.asignarForm.value.idMedidor || undefined
    };

    this.serviciosService.asignarServicio(dto).subscribe({
      next: () => {
        this.snackBar.open('Servicio asignado correctamente', 'Cerrar', { duration: 3000 });
        this.asignarForm.reset();
        this.cargarDatos(); // Recargar servicios asignados
        this.saving.set(false);
      },
      error: (error) => {
        console.error('Error al asignar servicio:', error);
        this.snackBar.open(
          error.error?.message || 'Error al asignar servicio',
          'Cerrar',
          { duration: 3000 }
        );
        this.saving.set(false);
      }
    });
  }

  /**
   * Desactiva un servicio
   */
  desactivarServicio(idInstalacionServicio: number): void {
    if (!confirm('¿Está seguro de desactivar este servicio?')) {
      return;
    }

    this.serviciosService.desactivarServicio(idInstalacionServicio).subscribe({
      next: () => {
        this.snackBar.open('Servicio desactivado correctamente', 'Cerrar', { duration: 3000 });
        this.cargarDatos(); // Recargar servicios
      },
      error: (error) => {
        console.error('Error al desactivar servicio:', error);
        this.snackBar.open('Error al desactivar servicio', 'Cerrar', { duration: 3000 });
      }
    });
  }

  /**
   * Cierra el diálogo
   */
  cerrar(): void {
    this.dialogRef.close();
  }
}
