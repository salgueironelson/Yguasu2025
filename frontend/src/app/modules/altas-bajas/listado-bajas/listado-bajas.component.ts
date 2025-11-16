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
import { Baja } from '@shared/models/altas-bajas.model';
import { HistorialDialogComponent } from '../dialogs/historial-dialog/historial-dialog.component';

@Component({
  selector: 'app-listado-bajas',
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
  templateUrl: './listado-bajas.component.html',
  styleUrls: ['./listado-bajas.component.scss']
})
export class ListadoBajasComponent implements OnInit {
  private altasBajasService = inject(AltasBajasService);
  private dialog = inject(MatDialog);

  bajas = signal<Baja[]>([]);
  bajasFiltradas = signal<Baja[]>([]);
  loading = signal(true);

  displayedColumns: string[] = [
    'idBaja',
    'codigoInstalacion',
    'nombreCliente',
    'fechaBaja',
    'motivo',
    'usuarioRegistro',
    'acciones'
  ];

  ngOnInit(): void {
    this.cargarBajas();
  }

  cargarBajas(): void {
    this.loading.set(true);

    this.altasBajasService.listarBajas().subscribe({
      next: (response) => {
        this.loading.set(false);
        if (response.success && response.data) {
          this.bajas.set(response.data);
          this.bajasFiltradas.set(response.data);
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
      this.bajasFiltradas.set(this.bajas());
      return;
    }

    const filtradas = this.bajas().filter(baja => {
      return (
        baja.codigoInstalacion?.toString().includes(valor) ||
        baja.nombreCliente?.toLowerCase().includes(valor) ||
        baja.motivo?.toLowerCase().includes(valor)
      );
    });

    this.bajasFiltradas.set(filtradas);
  }

  verHistorial(baja: Baja): void {
    if (!baja.codigoInstalacion) return;

    this.dialog.open(HistorialDialogComponent, {
      width: '900px',
      maxWidth: '95vw',
      maxHeight: '90vh',
      data: {
        codigoInstalacion: baja.codigoInstalacion,
        nombreCliente: baja.nombreCliente
      }
    });
  }

  getMotivoLabel(motivo?: string): string {
    const motivosMap: { [key: string]: string } = {
      'DEUDA': 'Deuda Impaga',
      'SOLICITUD_CLIENTE': 'Solicitud del Cliente',
      'FALTA_PAGO': 'Falta de Pago',
      'CAMBIO_TITULAR': 'Cambio de Titular',
      'PREDIO_DEMOLIDO': 'Predio Demolido',
      'IRREGULARIDAD': 'Irregularidad',
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
