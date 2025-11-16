import { Component, Inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MOTIVOS_BAJA } from '@shared/models/altas-bajas.model';

export interface BajaDialogData {
  codigoInstalacion: number;
  nombreCliente?: string;
}

@Component({
  selector: 'app-baja-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './baja-dialog.component.html',
  styleUrls: ['./baja-dialog.component.scss']
})
export class BajaDialogComponent {
  bajaForm: FormGroup;
  submitting = signal(false);
  motivosBaja = MOTIVOS_BAJA;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<BajaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: BajaDialogData
  ) {
    this.bajaForm = this.fb.group({
      motivo: ['', Validators.required],
      observaciones: ['', [Validators.minLength(10), Validators.maxLength(500)]]
    });
  }

  onSubmit(): void {
    if (this.bajaForm.valid && !this.submitting()) {
      this.submitting.set(true);
      this.dialogRef.close({
        codigoInstalacion: this.data.codigoInstalacion,
        motivo: this.bajaForm.value.motivo,
        observaciones: this.bajaForm.value.observaciones || null
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  getObservacionesError(): string {
    const control = this.bajaForm.get('observaciones');
    if (control?.hasError('minlength')) {
      return 'Las observaciones deben tener al menos 10 caracteres';
    }
    if (control?.hasError('maxlength')) {
      return 'Las observaciones no pueden exceder 500 caracteres';
    }
    return '';
  }

  getCharacterCount(): string {
    const length = this.bajaForm.get('observaciones')?.value?.length || 0;
    return `${length}/500`;
  }
}
