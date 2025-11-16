import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';

import { InstalacionService } from '@core/services/instalacion.service';
import { AltasBajasService } from '@core/services/altas-bajas.service';
import { InstalacionData } from '@shared/models/instalacion.model';
import { BajaDialogComponent } from '../dialogs/baja-dialog/baja-dialog.component';
import { AltaDialogComponent } from '../dialogs/alta-dialog/alta-dialog.component';
import { HistorialDialogComponent } from '../dialogs/historial-dialog/historial-dialog.component';

@Component({
  selector: 'app-gestion-altas-bajas',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatChipsModule
  ],
  templateUrl: './gestion-altas-bajas.component.html',
  styleUrls: ['./gestion-altas-bajas.component.scss']
})
export class GestionAltasBajasComponent {
  private fb = inject(FormBuilder);
  private instalacionService = inject(InstalacionService);
  private altasBajasService = inject(AltasBajasService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  searchForm: FormGroup;
  instalacion = signal<InstalacionData | null>(null);
  buscando = signal(false);
  estadoActual = signal<string | null>(null);

  constructor() {
    this.searchForm = this.fb.group({
      codigoInstalacion: ['']
    });
  }

  buscarInstalacion(): void {
    const codigo = this.searchForm.get('codigoInstalacion')?.value;

    if (!codigo) {
      this.snackBar.open('Ingrese un código de instalación', 'Cerrar', { duration: 3000 });
      return;
    }

    const codigoNumerico = parseInt(codigo, 10);
    if (isNaN(codigoNumerico) || codigoNumerico <= 0) {
      this.snackBar.open('El código debe ser un número válido', 'Cerrar', { duration: 3000 });
      return;
    }

    this.buscando.set(true);

    this.instalacionService.buscarPorCodigo(codigoNumerico).subscribe({
      next: (response) => {
        this.buscando.set(false);
        if (response.success && response.data) {
          this.instalacion.set(response.data);
          // Obtener historial para saber el estado actual
          this.obtenerEstadoActual(codigoNumerico);
        } else {
          this.snackBar.open('Instalación no encontrada', 'Cerrar', { duration: 3000 });
          this.limpiar();
        }
      },
      error: () => {
        this.buscando.set(false);
        this.snackBar.open('Error al buscar instalación', 'Cerrar', { duration: 3000 });
        this.limpiar();
      }
    });
  }

  obtenerEstadoActual(codigo: number): void {
    this.altasBajasService.obtenerHistorial(codigo).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.estadoActual.set(response.data.estadoActual || 'A');
        }
      },
      error: () => {
        // Si falla, asumir estado activo
        this.estadoActual.set('A');
      }
    });
  }

  darDeBaja(): void {
    if (!this.instalacion()) return;

    const dialogRef = this.dialog.open(BajaDialogComponent, {
      width: '600px',
      data: {
        codigoInstalacion: this.instalacion()!.codigoInstalacion,
        nombreCliente: this.instalacion()!.nombreCompleto
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.altasBajasService.darDeBaja(result).subscribe({
          next: (response) => {
            if (response.success) {
              this.snackBar.open('Instalación dada de baja exitosamente', 'Cerrar', {
                duration: 3000,
                panelClass: ['success-snackbar']
              });
              this.estadoActual.set('I');
            }
          },
          error: (error) => {
            const mensaje = error.error?.message || 'Error al dar de baja la instalación';
            this.snackBar.open(mensaje, 'Cerrar', { duration: 5000 });
          }
        });
      }
    });
  }

  darDeAlta(): void {
    if (!this.instalacion()) return;

    const dialogRef = this.dialog.open(AltaDialogComponent, {
      width: '600px',
      data: {
        codigoInstalacion: this.instalacion()!.codigoInstalacion,
        nombreCliente: this.instalacion()!.nombreCompleto
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.altasBajasService.darDeAlta(result).subscribe({
          next: (response) => {
            if (response.success) {
              this.snackBar.open('Instalación dada de alta exitosamente', 'Cerrar', {
                duration: 3000,
                panelClass: ['success-snackbar']
              });
              this.estadoActual.set('A');
            }
          },
          error: (error) => {
            const mensaje = error.error?.message || 'Error al dar de alta la instalación';
            this.snackBar.open(mensaje, 'Cerrar', { duration: 5000 });
          }
        });
      }
    });
  }

  verHistorial(): void {
    if (!this.instalacion()) return;

    this.dialog.open(HistorialDialogComponent, {
      width: '900px',
      maxWidth: '95vw',
      maxHeight: '90vh',
      data: {
        codigoInstalacion: this.instalacion()!.codigoInstalacion,
        nombreCliente: this.instalacion()!.nombreCompleto
      }
    });
  }

  limpiar(): void {
    this.instalacion.set(null);
    this.estadoActual.set(null);
  }

  estaActiva(): boolean {
    return this.estadoActual() === 'A';
  }

  estaInactiva(): boolean {
    return this.estadoActual() === 'I';
  }
}
