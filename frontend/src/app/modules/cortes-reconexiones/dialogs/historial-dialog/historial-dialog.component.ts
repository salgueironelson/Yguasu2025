import { Component, Inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { CortesReconexionesService } from '../../../../core/services/cortes-reconexiones.service';
import { HistorialCortesReconexiones, Corte, Reconexion } from '../../../../shared/models/cortes-reconexiones.model';

/**
 * Evento combinado de corte o reconexión para la línea de tiempo
 */
interface EventoHistorial {
  tipo: 'corte' | 'reconexion';
  fecha: Date;
  motivo: string;
  observaciones?: string;
  usuarioRegistro: string;
  id: number;
}

/**
 * Diálogo para mostrar el historial completo de cortes y reconexiones
 */
@Component({
  selector: 'app-historial-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatChipsModule
  ],
  templateUrl: './historial-dialog.component.html',
  styleUrls: ['./historial-dialog.component.scss']
})
export class HistorialDialogComponent implements OnInit {
  loading = signal(true);
  errorMessage = signal<string | null>(null);
  historial = signal<HistorialCortesReconexiones | null>(null);
  eventosCombinados = signal<EventoHistorial[]>([]);

  // Estadísticas
  totalCortes = signal(0);
  totalReconexiones = signal(0);
  totalMovimientos = signal(0);

  constructor(
    private dialogRef: MatDialogRef<HistorialDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { codigoInstalacion: number },
    private cortesReconexionesService: CortesReconexionesService
  ) {}

  ngOnInit(): void {
    this.cargarHistorial();
  }

  /**
   * Carga el historial de cortes y reconexiones
   */
  private cargarHistorial(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.cortesReconexionesService.obtenerHistorial(this.data.codigoInstalacion).subscribe({
      next: (response) => {
        this.loading.set(false);
        if (response.success && response.data) {
          this.historial.set(response.data);
          this.procesarHistorial(response.data);
          this.calcularEstadisticas(response.data);
        } else {
          this.errorMessage.set(response.message || 'No se pudo cargar el historial');
        }
      },
      error: (error) => {
        this.loading.set(false);
        this.errorMessage.set(
          error.error?.message || 'Error al cargar el historial. Por favor, intente nuevamente.'
        );
      }
    });
  }

  /**
   * Procesa y combina cortes y reconexiones en una línea de tiempo
   */
  private procesarHistorial(historial: HistorialCortesReconexiones): void {
    const eventos: EventoHistorial[] = [];

    // Agregar cortes
    historial.cortes.forEach((corte: Corte) => {
      eventos.push({
        tipo: 'corte',
        fecha: new Date(corte.fechaCorte),
        motivo: corte.motivo,
        observaciones: corte.observaciones,
        usuarioRegistro: corte.usuarioRegistro,
        id: corte.idCorte
      });
    });

    // Agregar reconexiones
    historial.reconexiones.forEach((reconexion: Reconexion) => {
      eventos.push({
        tipo: 'reconexion',
        fecha: new Date(reconexion.fechaReconexion),
        motivo: reconexion.motivo,
        observaciones: reconexion.observaciones,
        usuarioRegistro: reconexion.usuarioRegistro,
        id: reconexion.idReconexion
      });
    });

    // Ordenar por fecha descendente (más reciente primero)
    eventos.sort((a, b) => b.fecha.getTime() - a.fecha.getTime());

    this.eventosCombinados.set(eventos);
  }

  /**
   * Calcula estadísticas del historial
   */
  private calcularEstadisticas(historial: HistorialCortesReconexiones): void {
    this.totalCortes.set(historial.cortes.length);
    this.totalReconexiones.set(historial.reconexiones.length);
    this.totalMovimientos.set(historial.cortes.length + historial.reconexiones.length);
  }

  /**
   * Obtiene el icono según el tipo de evento
   */
  getIcono(tipo: 'corte' | 'reconexion'): string {
    return tipo === 'corte' ? 'power_off' : 'power';
  }

  /**
   * Obtiene la clase CSS según el tipo de evento
   */
  getClaseEvento(tipo: 'corte' | 'reconexion'): string {
    return tipo === 'corte' ? 'evento-corte' : 'evento-reconexion';
  }

  /**
   * Obtiene el badge de estado actual
   */
  getEstadoBadge(): { texto: string; clase: string } {
    const estado = this.historial()?.estadoActual;

    switch (estado) {
      case 'A':
        return { texto: 'Activo', clase: 'estado-activo' };
      case 'C':
        return { texto: 'Cortado', clase: 'estado-cortado' };
      case 'I':
        return { texto: 'Inactivo', clase: 'estado-inactivo' };
      default:
        return { texto: 'Desconocido', clase: 'estado-desconocido' };
    }
  }

  /**
   * Cierra el diálogo
   */
  onCerrar(): void {
    this.dialogRef.close();
  }
}
