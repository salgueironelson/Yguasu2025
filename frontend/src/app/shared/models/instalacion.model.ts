/**
 * Modelo de datos de instalación
 * Usado para autocompletar información al crear reclamos
 */
export interface InstalacionData {
  idInstalacion?: number;
  codigoInstalacion?: number;
  nombreCompleto?: string;
  calle?: string;
  categoria?: string;
  celular?: string;
  catastro?: string;
  medidor?: string;
}
