export interface Cliente {
  idUsuario?: number;
  idEstadoCivil: number;
  estadoCivil?: string;
  idTipoUsuario: number;
  tipoUsuario?: string;
  idDocDepartamento?: number;
  idTipoDocumento?: number;
  tipoDocumento?: string;
  paterno: string;
  materno: string;
  nombres: string;
  sexo: 'M' | 'F';
  ci?: string;
  nit?: string;
  fecNacimiento?: Date;
  direccion?: string;
  telefono?: string;
  fax?: string;
  celular?: string;
  nombreCompleto?: string;
  codAnterior?: number;
}
