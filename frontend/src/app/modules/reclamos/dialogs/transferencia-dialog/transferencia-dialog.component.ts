import { Component, Inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

export interface TransferenciaDialogData {
  reclamoId: number;
  reclamoNumero: number;
}

interface Usuario {
  id: number;
  nombre: string;
  departamento?: string;
}

@Component({
  selector: 'app-transferencia-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './transferencia-dialog.component.html',
  styleUrls: ['./transferencia-dialog.component.scss']
})
export class TransferenciaDialogComponent implements OnInit {
  transferenciaForm: FormGroup;
  submitting = signal(false);
  loadingUsuarios = signal(false);
  usuarios = signal<Usuario[]>([]);

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<TransferenciaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: TransferenciaDialogData
  ) {
    this.transferenciaForm = this.fb.group({
      usuarioDestino: [null, Validators.required],
      comentario: ['', [Validators.minLength(10), Validators.maxLength(500)]]
    });
  }

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.loadingUsuarios.set(true);

    // TODO: Implementar servicio real para obtener usuarios
    setTimeout(() => {
      this.usuarios.set([
        { id: 1, nombre: 'Juan Pérez', departamento: 'Técnico' },
        { id: 2, nombre: 'María García', departamento: 'Comercial' },
        { id: 3, nombre: 'Carlos López', departamento: 'Atención al Cliente' },
        { id: 4, nombre: 'Ana Martínez', departamento: 'Técnico' },
        { id: 5, nombre: 'Roberto Silva', departamento: 'Supervisión' }
      ]);
      this.loadingUsuarios.set(false);
    }, 500);
  }

  onSubmit(): void {
    if (this.transferenciaForm.valid && !this.submitting()) {
      this.submitting.set(true);
      this.dialogRef.close({
        usuarioDestino: this.transferenciaForm.value.usuarioDestino,
        comentario: this.transferenciaForm.value.comentario || null
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  getComentarioError(): string {
    const control = this.transferenciaForm.get('comentario');
    if (control?.hasError('minlength')) {
      return 'El comentario debe tener al menos 10 caracteres';
    }
    if (control?.hasError('maxlength')) {
      return 'El comentario no puede exceder 500 caracteres';
    }
    return '';
  }

  getCharacterCount(): string {
    const length = this.transferenciaForm.get('comentario')?.value?.length || 0;
    return `${length}/500`;
  }
}
