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
import { MOTIVOS_RECONEXION } from '../../../../shared/models/cortes-reconexiones.model';

/**
 * Diálogo para reconectar servicio de una instalación
 */
@Component({
  selector: 'app-reconexion-dialog',
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
  templateUrl: './reconexion-dialog.component.html',
  styleUrls: ['./reconexion-dialog.component.scss']
})
export class ReconexionDialogComponent {
  reconexionForm: FormGroup;
  loading = signal(false);
  errorMessage = signal<string | null>(null);
  motivos = MOTIVOS_RECONEXION;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ReconexionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { codigoInstalacion: number; nombreCliente: string },
    private cortesReconexionesService: CortesReconexionesService
  ) {
    this.reconexionForm = this.fb.group({
      codigoInstalacion: [{ value: data.codigoInstalacion, disabled: true }],
      nombreCliente: [{ value: data.nombreCliente, disabled: true }],
      motivo: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      observaciones: ['', [Validators.maxLength(500)]]
    });
  }

  /**
   * Reconecta el servicio
   */
  onReconectar(): void {
    if (this.reconexionForm.invalid) {
      this.reconexionForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    const reconexionData = {
      codigoInstalacion: this.data.codigoInstalacion,
      motivo: this.reconexionForm.value.motivo,
      observaciones: this.reconexionForm.value.observaciones
    };

    this.cortesReconexionesService.reconectarServicio(reconexionData).subscribe({
      next: (response) => {
        this.loading.set(false);
        if (response.success) {
          this.dialogRef.close(response.data);
        } else {
          this.errorMessage.set(response.message || 'Error al reconectar el servicio');
        }
      },
      error: (error) => {
        this.loading.set(false);
        this.errorMessage.set(
          error.error?.message || 'Error al reconectar el servicio. Por favor, intente nuevamente.'
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
    const control = this.reconexionForm.get(field);
    return !!(control && control.hasError(error) && control.touched);
  }
}
