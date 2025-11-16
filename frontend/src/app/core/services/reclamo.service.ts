import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { ApiResponse, PageResponse } from '@shared/models/api-response.model';
import {
  Reclamo,
  ReclamoCreate,
  ReclamoFilter,
  Paso,
  Comentario,
  Transferencia,
  Conclusion
} from '@shared/models/reclamo.model';

@Injectable({
  providedIn: 'root'
})
export class ReclamoService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/reclamos`;

  crear(reclamo: ReclamoCreate): Observable<ApiResponse<Reclamo>> {
    return this.http.post<ApiResponse<Reclamo>>(this.apiUrl, reclamo);
  }

  listar(
    filtros?: ReclamoFilter,
    page: number = 0,
    size: number = 10
  ): Observable<ApiResponse<PageResponse<Reclamo>>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (filtros) {
      if (filtros.busqueda) params = params.set('busqueda', filtros.busqueda);
      if (filtros.tipoReclamo) params = params.set('tipoReclamo', filtros.tipoReclamo);
      if (filtros.estado) params = params.set('estado', filtros.estado);
      if (filtros.departamento) params = params.set('departamento', filtros.departamento);
      if (filtros.procedente) params = params.set('procedente', filtros.procedente);
      if (filtros.usuarioActual) params = params.set('usuarioActual', filtros.usuarioActual.toString());
      if (filtros.fechaDesde) params = params.set('fechaDesde', filtros.fechaDesde.toISOString());
      if (filtros.fechaHasta) params = params.set('fechaHasta', filtros.fechaHasta.toISOString());
    }

    return this.http.get<ApiResponse<PageResponse<Reclamo>>>(this.apiUrl, { params });
  }

  obtenerPorId(id: number): Observable<ApiResponse<Reclamo>> {
    return this.http.get<ApiResponse<Reclamo>>(`${this.apiUrl}/${id}`);
  }

  obtenerHistorial(id: number): Observable<ApiResponse<Paso[]>> {
    return this.http.get<ApiResponse<Paso[]>>(`${this.apiUrl}/${id}/historial`);
  }

  agregarComentario(id: number, comentario: Comentario): Observable<ApiResponse<Reclamo>> {
    return this.http.post<ApiResponse<Reclamo>>(`${this.apiUrl}/${id}/comentario`, comentario);
  }

  transferir(id: number, transferencia: Transferencia): Observable<ApiResponse<Reclamo>> {
    return this.http.post<ApiResponse<Reclamo>>(`${this.apiUrl}/${id}/transferir`, transferencia);
  }

  concluir(id: number, conclusion: Conclusion): Observable<ApiResponse<Reclamo>> {
    return this.http.post<ApiResponse<Reclamo>>(`${this.apiUrl}/${id}/concluir`, conclusion);
  }
}
