import { Component, Inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { CortesReconexionesService } from '../../../../core/services/cortes-reconexiones.service';
import { MOTIVOS_CORTE } from '../../../../shared/models/cortes-reconexiones.model';

/**
 * Diálogo para cortar servicio de una instalación
 */
@Component({
  selector: 'app-corte-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatIconModule
  ],
  templateUrl: './corte-dialog.component.html',
  styleUrls: ['./corte-dialog.component.scss']
})
export class CorteDialogComponent {
  corteForm: FormGroup;
  loading = signal(false);
  errorMessage = signal<string | null>(null);
  motivos = MOTIVOS_CORTE;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CorteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { codigoInstalacion: number; nombreCliente: string },
    private cortesReconexionesService: CortesReconexionesService
  ) {
    this.corteForm = this.fb.group({
      codigoInstalacion: [{ value: data.codigoInstalacion, disabled: true }],
      nombreCliente: [{ value: data.nombreCliente, disabled: true }],
      motivo: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      observaciones: ['', [Validators.maxLength(500)]]
    });
  }

  /**
   * Corta el servicio
   */
  onCortar(): void {
    if (this.corteForm.invalid) {
      this.corteForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    const corteData = {
      codigoInstalacion: this.data.codigoInstalacion,
      motivo: this.corteForm.value.motivo,
      observaciones: this.corteForm.value.observaciones
    };

    this.cortesReconexionesService.cortarServicio(corteData).subscribe({
      next: (response) => {
        this.loading.set(false);
        if (response.success) {
          this.dialogRef.close(response.data);
        } else {
          this.errorMessage.set(response.message || 'Error al cortar el servicio');
        }
      },
      error: (error) => {
        this.loading.set(false);
        this.errorMessage.set(
          error.error?.message || 'Error al cortar el servicio. Por favor, intente nuevamente.'
        );
      }
    });
  }

  /**
   * Cierra el diálogo
   */
  onCancelar(): void {
    this.dialogRef.close();
  }

  /**
   * Verifica si un campo tiene error
   */
  hasError(field: string, error: string): boolean {
    const control = this.corteForm.get(field);
    return !!(control && control.hasError(error) && control.touched);
  }
}
