import { Component, Inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

export interface ComentarioDialogData {
  reclamoId: number;
  reclamoNumero: number;
}

@Component({
  selector: 'app-comentario-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './comentario-dialog.component.html',
  styleUrls: ['./comentario-dialog.component.scss']
})
export class ComentarioDialogComponent {
  comentarioForm: FormGroup;
  submitting = signal(false);

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ComentarioDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ComentarioDialogData
  ) {
    this.comentarioForm = this.fb.group({
      comentario: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(1000)]]
    });
  }

  onSubmit(): void {
    if (this.comentarioForm.valid && !this.submitting()) {
      this.submitting.set(true);
      this.dialogRef.close({
        comentario: this.comentarioForm.value.comentario
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  getErrorMessage(): string {
    const control = this.comentarioForm.get('comentario');
    if (control?.hasError('required')) {
      return 'El comentario es obligatorio';
    }
    if (control?.hasError('minlength')) {
      return 'El comentario debe tener al menos 10 caracteres';
    }
    if (control?.hasError('maxlength')) {
      return 'El comentario no puede exceder 1000 caracteres';
    }
    return '';
  }

  getCharacterCount(): string {
    const length = this.comentarioForm.get('comentario')?.value?.length || 0;
    return `${length}/1000`;
  }
}
