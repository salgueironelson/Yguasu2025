import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  CorteCreate,
  ReconexionCreate,
  Corte,
  Reconexion,
  HistorialCortesReconexiones,
  Plomero,
  FiltrosCorteMasivo,
  InstalacionConDeuda,
  CorteMasivoRequest,
  CorteMasivoResponse
} from '../../shared/models/cortes-reconexiones.model';
import { ApiResponse } from '../../shared/models/api-response.model';

/**
 * Servicio para gestión de cortes y reconexiones de servicio
 */
@Injectable({
  providedIn: 'root'
})
export class CortesReconexionesService {
  private apiUrl = `${environment.apiUrl}/cortes-reconexiones`;

  constructor(private http: HttpClient) {}

  /**
   * Corta el servicio de una instalación
   */
  cortarServicio(data: CorteCreate): Observable<ApiResponse<Corte>> {
    return this.http.post<ApiResponse<Corte>>(`${this.apiUrl}/cortes`, data);
  }

  /**
   * Reconecta el servicio de una instalación
   */
  reconectarServicio(data: ReconexionCreate): Observable<ApiResponse<Reconexion>> {
    return this.http.post<ApiResponse<Reconexion>>(`${this.apiUrl}/reconexiones`, data);
  }

  /**
   * Obtiene el historial completo de cortes y reconexiones de una instalación
   */
  obtenerHistorial(codigoInstalacion: number): Observable<ApiResponse<HistorialCortesReconexiones>> {
    return this.http.get<ApiResponse<HistorialCortesReconexiones>>(
      `${this.apiUrl}/historial/${codigoInstalacion}`
    );
  }

  /**
   * Lista todos los cortes registrados
   */
  listarCortes(): Observable<ApiResponse<Corte[]>> {
    return this.http.get<ApiResponse<Corte[]>>(`${this.apiUrl}/cortes`);
  }

  /**
   * Lista todas las reconexiones registradas
   */
  listarReconexiones(): Observable<ApiResponse<Reconexion[]>> {
    return this.http.get<ApiResponse<Reconexion[]>>(`${this.apiUrl}/reconexiones`);
  }

  /**
   * Lista todos los plomeros activos
   */
  listarPlomerosActivos(): Observable<ApiResponse<Plomero[]>> {
    return this.http.get<ApiResponse<Plomero[]>>(`${this.apiUrl}/plomeros`);
  }

  /**
   * Busca instalaciones con deuda según filtros
   */
  buscarInstalacionesConDeuda(filtros: FiltrosCorteMasivo): Observable<ApiResponse<InstalacionConDeuda[]>> {
    return this.http.post<ApiResponse<InstalacionConDeuda[]>>(
      `${this.apiUrl}/buscar-deudores`,
      filtros
    );
  }

  /**
   * Realiza corte masivo de servicios
   */
  corteMasivo(request: CorteMasivoRequest): Observable<ApiResponse<CorteMasivoResponse>> {
    return this.http.post<ApiResponse<CorteMasivoResponse>>(
      `${this.apiUrl}/corte-masivo`,
      request
    );
  }
}
