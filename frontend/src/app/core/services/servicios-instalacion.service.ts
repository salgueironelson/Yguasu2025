import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  Servicio,
  InstalacionServicio,
  AsignarServicioDTO,
  Medidor
} from '../../shared/models/servicio.model';

/**
 * Servicio para gestión de servicios de instalaciones
 */
@Injectable({
  providedIn: 'root'
})
export class ServiciosInstalacionService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/servicios-instalacion`;

  /**
   * Lista servicios disponibles para asignar
   */
  listarServiciosDisponibles(): Observable<Servicio[]> {
    return this.http.get<Servicio[]>(`${this.apiUrl}/servicios-disponibles`);
  }

  /**
   * Lista servicios asignados a una instalación
   */
  listarServiciosPorInstalacion(idInstalacion: number): Observable<InstalacionServicio[]> {
    return this.http.get<InstalacionServicio[]>(`${this.apiUrl}/instalacion/${idInstalacion}`);
  }

  /**
   * Lista medidores disponibles
   */
  listarMedidoresDisponibles(): Observable<Medidor[]> {
    return this.http.get<Medidor[]>(`${this.apiUrl}/medidores-disponibles`);
  }

  /**
   * Asigna un servicio a una instalación
   */
  asignarServicio(dto: AsignarServicioDTO): Observable<InstalacionServicio> {
    return this.http.post<InstalacionServicio>(this.apiUrl, dto);
  }

  /**
   * Desactiva un servicio de una instalación
   */
  desactivarServicio(idInstalacionServicio: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${idInstalacionServicio}`);
  }
}
