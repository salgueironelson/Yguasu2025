import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  UsuarioCreate,
  Usuario,
  InstalacionCreate,
  Instalacion,
  UsuarioConInstalaciones
} from '../../shared/models/usuario.model';
import { ApiResponse } from '../../shared/models/api-response.model';

/**
 * Servicio para gestión de usuarios e instalaciones
 */
@Injectable({
  providedIn: 'root'
})
export class UsuariosInstalacionesService {
  private apiUrl = `${environment.apiUrl}/usuarios-instalaciones`;

  constructor(private http: HttpClient) {}

  /**
   * Crea un nuevo usuario
   */
  crearUsuario(data: UsuarioCreate): Observable<ApiResponse<Usuario>> {
    return this.http.post<ApiResponse<Usuario>>(`${this.apiUrl}/usuarios`, data);
  }

  /**
   * Crea una nueva instalación
   */
  crearInstalacion(data: InstalacionCreate): Observable<ApiResponse<Instalacion>> {
    return this.http.post<ApiResponse<Instalacion>>(`${this.apiUrl}/instalaciones`, data);
  }

  /**
   * Lista todos los usuarios
   */
  listarUsuarios(): Observable<ApiResponse<Usuario[]>> {
    return this.http.get<ApiResponse<Usuario[]>>(`${this.apiUrl}/usuarios`);
  }

  /**
   * Busca usuarios por término
   */
  buscarUsuarios(busqueda: string): Observable<ApiResponse<Usuario[]>> {
    return this.http.get<ApiResponse<Usuario[]>>(`${this.apiUrl}/usuarios/buscar`, {
      params: { busqueda }
    });
  }

  /**
   * Obtiene usuario completo con instalaciones
   */
  obtenerUsuarioCompleto(idUsuario: number): Observable<ApiResponse<UsuarioConInstalaciones>> {
    return this.http.get<ApiResponse<UsuarioConInstalaciones>>(
      `${this.apiUrl}/usuarios/${idUsuario}/completo`
    );
  }

  /**
   * Lista instalaciones de un usuario
   */
  listarInstalacionesPorUsuario(idUsuario: number): Observable<ApiResponse<Instalacion[]>> {
    return this.http.get<ApiResponse<Instalacion[]>>(
      `${this.apiUrl}/usuarios/${idUsuario}/instalaciones`
    );
  }
}
