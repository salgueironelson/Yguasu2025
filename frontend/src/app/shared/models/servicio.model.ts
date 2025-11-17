/**
 * Modelo para servicios
 */

export interface Servicio {
  idServicio: number;
  servicio: string;
  idTipoServicio: number;
}

export interface InstalacionServicio {
  idInstalacionServicio: number;
  idInstalacion: number;
  idServicio: number;
  nombreServicio: string;
  idMedidor?: number;
  serieMedidor?: string;
  estado: string;
  estadoDescripcion: string;
}

export interface AsignarServicioDTO {
  idInstalacion: number;
  idServicio: number;
  idMedidor?: number;
}

export interface Medidor {
  idMedidor: number;
  serie: string;
}
