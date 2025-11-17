import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTabsModule } from '@angular/material/tabs';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { UsuariosInstalacionesService } from '../../../../core/services/usuarios-instalaciones.service';
import { Usuario, Instalacion, UsuarioConInstalaciones, ESTADOS_CIVILES, TIPOS_USUARIO, CATEGORIAS } from '../../../../shared/models/usuario.model';
import { ServiciosInstalacionDialogComponent } from '../../dialogs/servicios-instalacion-dialog/servicios-instalacion-dialog.component';

/**
 * Componente para gestión de usuarios e instalaciones
 */
@Component({
  selector: 'app-gestion-usuarios',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatSnackBarModule,
    MatTabsModule,
    MatExpansionModule,
    MatChipsModule,
    MatTooltipModule
  ],
  templateUrl: './gestion-usuarios.component.html',
  styleUrls: ['./gestion-usuarios.component.scss']
})
export class GestionUsuariosComponent implements OnInit {
  // Forms
  usuarioForm: FormGroup;
  instalacionForm: FormGroup;
  searchForm: FormGroup;

  // Data
  usuarios = signal<Usuario[]>([]);
  selectedUsuario = signal<Usuario | null>(null);
  usuarioConInstalaciones = signal<UsuarioConInstalaciones | null>(null);
  instalaciones = signal<Instalacion[]>([]);

  // States
  loading = signal(false);
  saving = signal(false);
  loadingInstalaciones = signal(false);

  // Catalogs
  estadosCiviles = ESTADOS_CIVILES;
  tiposUsuario = TIPOS_USUARIO;
  categorias = CATEGORIAS;

  // Table columns
  usuariosColumns = ['nombreCompleto', 'ci', 'celular', 'totalInstalaciones', 'acciones'];
  instalacionesColumns = ['codigoInstalacion', 'direccion', 'estado', 'acciones'];

  constructor(
    private fb: FormBuilder,
    private service: UsuariosInstalacionesService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {
    this.usuarioForm = this.fb.group({
      paterno: ['', [Validators.required, Validators.maxLength(100)]],
      materno: ['', [Validators.maxLength(100)]],
      nombres: ['', [Validators.required, Validators.maxLength(100)]],
      sexo: ['M', [Validators.required]],
      ci: ['', [Validators.required, Validators.maxLength(20)]],
      nit: ['', [Validators.maxLength(20)]],
      fecNacimiento: [''],
      direccion: ['', [Validators.required, Validators.maxLength(200)]],
      telefono: ['', [Validators.maxLength(20)]],
      celular: ['', [Validators.maxLength(20)]],
      idEstadoCivil: [1, [Validators.required]],
      idTipoUsuario: [1, [Validators.required]]
    });

    this.instalacionForm = this.fb.group({
      codigoInstalacion: ['', [Validators.required]],
      idUsuario: ['', [Validators.required]],
      idCategoria: [1, [Validators.required]],
      zona: [''],
      direccion: ['', [Validators.maxLength(200)]],
      celular: ['', [Validators.maxLength(20)]]
    });

    this.searchForm = this.fb.group({
      busqueda: ['']
    });
  }

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  /**
   * Carga todos los usuarios
   */
  cargarUsuarios(): void {
    this.loading.set(true);
    this.service.listarUsuarios().subscribe({
      next: (response) => {
        this.loading.set(false);
        if (response.success && response.data) {
          this.usuarios.set(response.data);
        }
      },
      error: (error) => {
        this.loading.set(false);
        this.mostrarError('Error al cargar usuarios');
      }
    });
  }

  /**
   * Buscar usuarios
   */
  onBuscar(): void {
    const busqueda = this.searchForm.value.busqueda?.trim();
    if (!busqueda) {
      this.cargarUsuarios();
      return;
    }

    this.loading.set(true);
    this.service.buscarUsuarios(busqueda).subscribe({
      next: (response) => {
        this.loading.set(false);
        if (response.success && response.data) {
          this.usuarios.set(response.data);
        }
      },
      error: (error) => {
        this.loading.set(false);
        this.mostrarError('Error en la búsqueda');
      }
    });
  }

  /**
   * Crear usuario
   */
  onCrearUsuario(): void {
    if (this.usuarioForm.invalid) {
      this.usuarioForm.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    this.service.crearUsuario(this.usuarioForm.value).subscribe({
      next: (response) => {
        this.saving.set(false);
        if (response.success) {
          this.mostrarExito('Usuario creado exitosamente');
          this.usuarioForm.reset({
            sexo: 'M',
            idEstadoCivil: 1,
            idTipoUsuario: 1
          });
          this.cargarUsuarios();
        } else {
          this.mostrarError(response.message || 'Error al crear usuario');
        }
      },
      error: (error) => {
        this.saving.set(false);
        this.mostrarError(error.error?.message || 'Error al crear usuario');
      }
    });
  }

  /**
   * Crear instalación
   */
  onCrearInstalacion(): void {
    if (this.instalacionForm.invalid) {
      this.instalacionForm.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    this.service.crearInstalacion(this.instalacionForm.value).subscribe({
      next: (response) => {
        this.saving.set(false);
        if (response.success) {
          this.mostrarExito('Instalación creada exitosamente');
          this.instalacionForm.reset({ idCategoria: 1 });
          this.cargarUsuarios();
        } else {
          this.mostrarError(response.message || 'Error al crear instalación');
        }
      },
      error: (error) => {
        this.saving.set(false);
        this.mostrarError(error.error?.message || 'Error al crear instalación');
      }
    });
  }

  /**
   * Seleccionar usuario para crear instalación
   */
  seleccionarUsuario(usuario: Usuario): void {
    this.selectedUsuario.set(usuario);
    this.instalacionForm.patchValue({
      idUsuario: usuario.idUsuario
    });
  }

  /**
   * Ver instalaciones de un usuario
   */
  verInstalaciones(usuario: Usuario): void {
    this.loadingInstalaciones.set(true);
    this.service.obtenerUsuarioConInstalaciones(usuario.idUsuario).subscribe({
      next: (response) => {
        this.loadingInstalaciones.set(false);
        if (response.success && response.data) {
          this.usuarioConInstalaciones.set(response.data);
          this.instalaciones.set(response.data.instalaciones);
        }
      },
      error: (error) => {
        this.loadingInstalaciones.set(false);
        this.mostrarError('Error al cargar instalaciones');
      }
    });
  }

  /**
   * Abrir diálogo de gestión de servicios
   */
  gestionarServicios(instalacion: Instalacion): void {
    this.dialog.open(ServiciosInstalacionDialogComponent, {
      width: '800px',
      data: {
        idInstalacion: instalacion.idInstalacion,
        codigoInstalacion: instalacion.codigoInstalacion
      }
    });
  }

  private mostrarExito(mensaje: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: ['snackbar-success']
    });
  }

  private mostrarError(mensaje: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 5000,
      panelClass: ['snackbar-error']
    });
  }
}
