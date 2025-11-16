import { Component, Inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

export interface ConclusionDialogData {
  reclamoId: number;
  reclamoNumero: number;
}

@Component({
  selector: 'app-conclusion-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './conclusion-dialog.component.html',
  styleUrls: ['./conclusion-dialog.component.scss']
})
export class ConclusionDialogComponent {
  conclusionForm: FormGroup;
  submitting = signal(false);

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ConclusionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConclusionDialogData
  ) {
    this.conclusionForm = this.fb.group({
      procedente: [null, Validators.required],
      conclusion: ['', [Validators.required, Validators.minLength(20), Validators.maxLength(1000)]]
    });
  }

  onSubmit(): void {
    if (this.conclusionForm.valid && !this.submitting()) {
      this.submitting.set(true);
      this.dialogRef.close({
        procedente: this.conclusionForm.value.procedente,
        conclusion: this.conclusionForm.value.conclusion
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  getConclusionError(): string {
    const control = this.conclusionForm.get('conclusion');
    if (control?.hasError('required')) {
      return 'La conclusión es obligatoria';
    }
    if (control?.hasError('minlength')) {
      return 'La conclusión debe tener al menos 20 caracteres';
    }
    if (control?.hasError('maxlength')) {
      return 'La conclusión no puede exceder 1000 caracteres';
    }
    return '';
  }

  getCharacterCount(): string {
    const length = this.conclusionForm.get('conclusion')?.value?.length || 0;
    return `${length}/1000`;
  }

  getProcedenteIcon(): string {
    const procedente = this.conclusionForm.get('procedente')?.value;
    if (procedente === true) {
      return 'check_circle';
    } else if (procedente === false) {
      return 'cancel';
    }
    return 'help_outline';
  }
}
