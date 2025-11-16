import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { ApiResponse } from '@shared/models/api-response.model';
import { InstalacionData } from '@shared/models/instalacion.model';

/**
 * Servicio para gestión de instalaciones
 */
@Injectable({
  providedIn: 'root'
})
export class InstalacionService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/instalaciones`;

  /**
   * Busca una instalación por su código
   * Retorna todos los datos necesarios para crear un reclamo
   *
   * @param codigo Código de instalación
   * @returns Observable con los datos de la instalación
   */
  buscarPorCodigo(codigo: number): Observable<ApiResponse<InstalacionData>> {
    return this.http.get<ApiResponse<InstalacionData>>(`${this.apiUrl}/buscar/${codigo}`);
  }
}
