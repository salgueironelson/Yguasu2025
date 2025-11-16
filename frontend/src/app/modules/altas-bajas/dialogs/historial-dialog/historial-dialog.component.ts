import { Component, Inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatTabsModule } from '@angular/material/tabs';
import { AltasBajasService } from '@core/services/altas-bajas.service';
import { HistorialAltasBajas, Alta, Baja } from '@shared/models/altas-bajas.model';

export interface HistorialDialogData {
  codigoInstalacion: number;
  nombreCliente?: string;
}

interface MovimientoTimeline {
  tipo: 'ALTA' | 'BAJA';
  fecha: Date;
  motivo: string;
  observaciones?: string;
  usuarioRegistro?: string;
  id: number;
}

@Component({
  selector: 'app-historial-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatTabsModule
  ],
  templateUrl: './historial-dialog.component.html',
  styleUrls: ['./historial-dialog.component.scss']
})
export class HistorialDialogComponent implements OnInit {
  historial = signal<HistorialAltasBajas | null>(null);
  timeline = signal<MovimientoTimeline[]>([]);
  loading = signal(true);
  totalAltas = signal(0);
  totalBajas = signal(0);

  constructor(
    private altasBajasService: AltasBajasService,
    private dialogRef: MatDialogRef<HistorialDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: HistorialDialogData
  ) {}

  ngOnInit(): void {
    this.cargarHistorial();
  }

  cargarHistorial(): void {
    this.loading.set(true);

    this.altasBajasService.obtenerHistorial(this.data.codigoInstalacion).subscribe({
      next: (response) => {
        this.loading.set(false);
        if (response.success && response.data) {
          this.historial.set(response.data);
          this.procesarTimeline(response.data);
          this.totalAltas.set(response.data.altas?.length || 0);
          this.totalBajas.set(response.data.bajas?.length || 0);
        }
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  procesarTimeline(historial: HistorialAltasBajas): void {
    const movimientos: MovimientoTimeline[] = [];

    // Agregar bajas
    historial.bajas?.forEach(baja => {
      movimientos.push({
        tipo: 'BAJA',
        fecha: new Date(baja.fechaBaja!),
        motivo: baja.motivo || 'Sin motivo',
        observaciones: baja.observaciones,
        usuarioRegistro: baja.usuarioRegistro,
        id: baja.idBaja!
      });
    });

    // Agregar altas
    historial.altas?.forEach(alta => {
      movimientos.push({
        tipo: 'ALTA',
        fecha: new Date(alta.fechaAlta!),
        motivo: alta.motivo || 'Sin motivo',
        observaciones: alta.observaciones,
        usuarioRegistro: alta.usuarioRegistro,
        id: alta.idAlta!
      });
    });

    // Ordenar por fecha (más reciente primero)
    movimientos.sort((a, b) => b.fecha.getTime() - a.fecha.getTime());

    this.timeline.set(movimientos);
  }

  getEstadoActualLabel(): string {
    const estado = this.historial()?.estadoActual;
    return estado === 'A' ? 'ACTIVO' : estado === 'I' ? 'INACTIVO' : 'DESCONOCIDO';
  }

  getEstadoActualColor(): string {
    const estado = this.historial()?.estadoActual;
    return estado === 'A' ? 'primary' : 'warn';
  }

  getMotivoLabel(motivo: string): string {
    const motivosMap: { [key: string]: string } = {
      'DEUDA': 'Deuda Impaga',
      'SOLICITUD_CLIENTE': 'Solicitud del Cliente',
      'FALTA_PAGO': 'Falta de Pago',
      'CAMBIO_TITULAR': 'Cambio de Titular',
      'PREDIO_DEMOLIDO': 'Predio Demolido/Inhabitable',
      'IRREGULARIDAD': 'Irregularidad Detectada',
      'PAGO_DEUDA': 'Pago de Deuda',
      'REGULARIZACION': 'Regularización',
      'NUEVO_TITULAR': 'Nuevo Titular',
      'REACTIVACION': 'Reactivación de Servicio',
      'OTRO': 'Otro Motivo'
    };

    return motivosMap[motivo] || motivo;
  }

  formatFecha(fecha: Date): string {
    return new Date(fecha).toLocaleString('es-BO', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  formatFechaCorta(fecha: Date): string {
    return new Date(fecha).toLocaleDateString('es-BO', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }

  cerrar(): void {
    this.dialogRef.close();
  }
}
