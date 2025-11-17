/**
 * Modelos para gestión de usuarios e instalaciones
 */

/**
 * DTO para crear un usuario
 */
export interface UsuarioCreate {
  paterno: string;
  materno?: string;
  nombres: string;
  sexo: string;
  ci: string;
  nit?: string;
  fecNacimiento?: string;
  direccion: string;
  telefono?: string;
  celular?: string;
  idEstadoCivil: number;
  idTipoUsuario: number;
  idTipoDocumento?: number;
  idDocDepartamento?: number;
}

/**
 * DTO para respuesta de usuario
 */
export interface Usuario {
  idUsuario: number;
  paterno: string;
  materno: string;
  nombres: string;
  nombreCompleto: string;
  sexo: string;
  ci: string;
  nit?: string;
  fecNacimiento?: string;
  direccion: string;
  telefono?: string;
  celular?: string;
  idEstadoCivil: number;
  estadoCivil: string;
  idTipoUsuario: number;
  tipoUsuario: string;
  totalInstalaciones: number;
}

/**
 * DTO para crear instalación
 */
export interface InstalacionCreate {
  codigoInstalacion: number;
  idUsuario: number;
  idCalle?: number;
  idCategoria: number;
  idZona?: number;
  idRuta?: number;
  idManzana?: number;
  idSecuencia?: number;
  direccion?: string;
  celular?: string;
  zona?: string;
}

/**
 * DTO para respuesta de instalación
 */
export interface Instalacion {
  idInstalacion: number;
  codigoInstalacion: number;
  idUsuario: number;
  nombreCliente: string;
  direccion?: string;
  zona?: string;
  celular?: string;
  estado: string;
  estadoDescripcion: string;
  idCategoria: number;
  categoriaDescripcion?: string;
}

/**
 * DTO para usuario con instalaciones
 */
export interface UsuarioConInstalaciones {
  idUsuario: number;
  nombreCompleto: string;
  ci: string;
  celular?: string;
  direccion: string;
  instalaciones: Instalacion[];
  totalInstalaciones: number;
  instalacionesActivas: number;
  instalacionesCortadas: number;
  instalacionesInactivas: number;
}

/**
 * Estados civiles
 */
export const ESTADOS_CIVILES = [
  { id: 1, descripcion: 'Soltero/a' },
  { id: 2, descripcion: 'Casado/a' },
  { id: 3, descripcion: 'Divorciado/a' },
  { id: 4, descripcion: 'Viudo/a' },
  { id: 5, descripcion: 'Conviviente' }
];

/**
 * Tipos de usuario
 */
export const TIPOS_USUARIO = [
  { id: 1, descripcion: 'Residencial' },
  { id: 2, descripcion: 'Comercial' },
  { id: 3, descripcion: 'Industrial' },
  { id: 4, descripcion: 'Público' }
];

/**
 * Categorías de instalación
 */
export const CATEGORIAS = [
  { id: 1, descripcion: 'Residencial' },
  { id: 2, descripcion: 'Comercial' },
  { id: 3, descripcion: 'Industrial' },
  { id: 4, descripcion: 'Social' }
];
