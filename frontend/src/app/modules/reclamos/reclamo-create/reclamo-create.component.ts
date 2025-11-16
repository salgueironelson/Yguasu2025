import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ToastrService } from 'ngx-toastr';

import { ReclamoService } from '@core/services/reclamo.service';
import { TIPOS_RECLAMO } from '@shared/models/reclamo.model';

@Component({
  selector: 'app-reclamo-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
    MatCheckboxModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './reclamo-create.component.html',
  styleUrls: ['./reclamo-create.component.scss']
})
export class ReclamoCreateComponent {
  private fb = inject(FormBuilder);
  private reclamoService = inject(ReclamoService);
  private router = inject(Router);
  private toastr = inject(ToastrService);

  reclamoForm: FormGroup;
  loading = signal(false);
  tieneInstalacion = signal(true);
  tiposReclamo = TIPOS_RECLAMO;

  // Datos del cliente (cuando se busca por código)
  datosCliente = signal<any>(null);

  constructor() {
    this.reclamoForm = this.fb.group({
      // Para reclamos con instalación
      codigoInstalacion: [''],

      // Datos que se autocomple tan si tiene instalación
      nombreCliente: [{ value: '', disabled: true }],
      numeroMedidor: [{ value: '', disabled: true }],
      catastro: [{ value: '', disabled: true }],
      categoria: [{ value: '', disabled: true }],

      // Campos obligatorios
      reclamante: ['', Validators.required],
      celular: ['', [Validators.required, Validators.pattern(/^[0-9]{7,8}$/)]],
      tipoReclamo: ['', Validators.required],
      fechaReclamo: [new Date(), Validators.required],
      detalle: ['', [Validators.required, Validators.minLength(10)]],

      // Opcionales
      ubicacion: [''],
      departamento: ['']
    });
  }

  toggleTipoReclamo(): void {
    this.tieneInstalacion.set(!this.tieneInstalacion());

    if (this.tieneInstalacion()) {
      this.reclamoForm.get('codigoInstalacion')?.setValidators([Validators.required]);
    } else {
      this.reclamoForm.get('codigoInstalacion')?.clearValidators();
      this.reclamoForm.patchValue({
        codigoInstalacion: '',
        nombreCliente: '',
        numeroMedidor: '',
        catastro: '',
        categoria: ''
      });
      this.datosCliente.set(null);
    }
    this.reclamoForm.get('codigoInstalacion')?.updateValueAndValidity();
  }

  buscarCliente(): void {
    const codigo = this.reclamoForm.get('codigoInstalacion')?.value;

    if (!codigo) {
      this.toastr.warning('Ingrese un código de instalación');
      return;
    }

    // TODO: Implementar búsqueda de cliente por código
    // Por ahora simulamos con datos de prueba
    this.datosCliente.set({
      nombre: 'Juan Pérez García',
      medidor: '12345678',
      catastro: 'Z1-R2-M3-C4',
      categoria: 'Residencial'
    });

    this.reclamoForm.patchValue({
      nombreCliente: this.datosCliente()?.nombre,
      numeroMedidor: this.datosCliente()?.medidor,
      catastro: this.datosCliente()?.catastro,
      categoria: this.datosCliente()?.categoria,
      reclamante: this.datosCliente()?.nombre
    });
  }

  onSubmit(): void {
    if (this.reclamoForm.invalid) {
      this.toastr.error('Complete todos los campos obligatorios');
      Object.keys(this.reclamoForm.controls).forEach(key => {
        const control = this.reclamoForm.get(key);
        if (control?.invalid) {
          control.markAsTouched();
        }
      });
      return;
    }

    this.loading.set(true);

    const formValue = this.reclamoForm.getRawValue();
    const reclamo = {
      codigoInstalacion: this.tieneInstalacion() ? formValue.codigoInstalacion : null,
      celular: formValue.celular,
      tipoReclamo: formValue.tipoReclamo,
      fechaReclamo: formValue.fechaReclamo,
      detalle: formValue.detalle,
      reclamante: formValue.reclamante,
      ubicacion: formValue.ubicacion,
      departamento: formValue.departamento
    };

    this.reclamoService.crear(reclamo).subscribe({
      next: (response) => {
        this.loading.set(false);
        if (response.success) {
          this.toastr.success(response.message || 'Reclamo creado exitosamente');
          this.router.navigate(['/dashboard/reclamos']);
        }
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/dashboard/reclamos']);
  }
}
