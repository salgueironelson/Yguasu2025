import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDialog } from '@angular/material/dialog';
import { AltasBajasService } from '@core/services/altas-bajas.service';
import { Alta } from '@shared/models/altas-bajas.model';
import { HistorialDialogComponent } from '../dialogs/historial-dialog/historial-dialog.component';

@Component({
  selector: 'app-listado-altas',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './listado-altas.component.html',
  styleUrls: ['./listado-altas.component.scss']
})
export class ListadoAltasComponent implements OnInit {
  private altasBajasService = inject(AltasBajasService);
  private dialog = inject(MatDialog);

  altas = signal<Alta[]>([]);
  altasFiltradas = signal<Alta[]>([]);
  loading = signal(true);

  displayedColumns: string[] = [
    'idAlta',
    'codigoInstalacion',
    'nombreCliente',
    'fechaAlta',
    'motivo',
    'usuarioRegistro',
    'acciones'
  ];

  ngOnInit(): void {
    this.cargarAltas();
  }

  cargarAltas(): void {
    this.loading.set(true);

    this.altasBajasService.listarAltas().subscribe({
      next: (response) => {
        this.loading.set(false);
        if (response.success && response.data) {
          this.altas.set(response.data);
          this.altasFiltradas.set(response.data);
        }
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  aplicarFiltro(event: Event): void {
    const valor = (event.target as HTMLInputElement).value.toLowerCase();

    if (!valor) {
      this.altasFiltradas.set(this.altas());
      return;
    }

    const filtradas = this.altas().filter(alta => {
      return (
        alta.codigoInstalacion?.toString().includes(valor) ||
        alta.nombreCliente?.toLowerCase().includes(valor) ||
        alta.motivo?.toLowerCase().includes(valor)
      );
    });

    this.altasFiltradas.set(filtradas);
  }

  verHistorial(alta: Alta): void {
    if (!alta.codigoInstalacion) return;

    this.dialog.open(HistorialDialogComponent, {
      width: '900px',
      maxWidth: '95vw',
      maxHeight: '90vh',
      data: {
        codigoInstalacion: alta.codigoInstalacion,
        nombreCliente: alta.nombreCliente
      }
    });
  }

  getMotivoLabel(motivo?: string): string {
    const motivosMap: { [key: string]: string } = {
      'PAGO_DEUDA': 'Pago de Deuda',
      'SOLICITUD_CLIENTE': 'Solicitud del Cliente',
      'REGULARIZACION': 'Regularización',
      'NUEVO_TITULAR': 'Nuevo Titular',
      'REACTIVACION': 'Reactivación',
      'OTRO': 'Otro'
    };

    return motivo ? (motivosMap[motivo] || motivo) : '-';
  }

  formatFecha(fecha?: Date): string {
    if (!fecha) return '-';
    return new Date(fecha).toLocaleString('es-BO', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
