import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { ApiResponse } from '@shared/models/api-response.model';
import {
  Alta,
  AltaCreate,
  Baja,
  BajaCreate,
  HistorialAltasBajas
} from '@shared/models/altas-bajas.model';

/**
 * Servicio para gestión de altas y bajas de instalaciones
 */
@Injectable({
  providedIn: 'root'
})
export class AltasBajasService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/altas-bajas`;

  /**
   * Da de baja una instalación
   */
  darDeBaja(baja: BajaCreate): Observable<ApiResponse<Baja>> {
    return this.http.post<ApiResponse<Baja>>(`${this.apiUrl}/bajas`, baja);
  }

  /**
   * Da de alta una instalación
   */
  darDeAlta(alta: AltaCreate): Observable<ApiResponse<Alta>> {
    return this.http.post<ApiResponse<Alta>>(`${this.apiUrl}/altas`, alta);
  }

  /**
   * Obtiene el historial de altas y bajas de una instalación
   */
  obtenerHistorial(codigoInstalacion: number): Observable<ApiResponse<HistorialAltasBajas>> {
    return this.http.get<ApiResponse<HistorialAltasBajas>>(
      `${this.apiUrl}/historial/${codigoInstalacion}`
    );
  }

  /**
   * Lista todas las bajas registradas
   */
  listarBajas(): Observable<ApiResponse<Baja[]>> {
    return this.http.get<ApiResponse<Baja[]>>(`${this.apiUrl}/bajas`);
  }

  /**
   * Lista todas las altas registradas
   */
  listarAltas(): Observable<ApiResponse<Alta[]>> {
    return this.http.get<ApiResponse<Alta[]>>(`${this.apiUrl}/altas`);
  }
}
