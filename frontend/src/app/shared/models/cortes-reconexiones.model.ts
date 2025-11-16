/**
 * Modelos para gestión de cortes y reconexiones de servicio
 */

/**
 * DTO para crear un corte de servicio
 */
export interface CorteCreate {
  codigoInstalacion: number;
  motivo: string;
  observaciones?: string;
  idPlomero?: number;
}

/**
 * DTO para crear una reconexión de servicio
 */
export interface ReconexionCreate {
  codigoInstalacion: number;
  motivo: string;
  observaciones?: string;
}

/**
 * DTO para respuesta de corte
 */
export interface Corte {
  idCorte: number;
  idInstalacion: number;
  codigoInstalacion: number;
  nombreCliente: string;
  fechaCorte: string;
  motivo: string;
  observaciones?: string;
  idPlomero?: number;
  nombrePlomero?: string;
  usuarioRegistro: string;
  estado: string;
}

/**
 * DTO para respuesta de reconexión
 */
export interface Reconexion {
  idReconexion: number;
  idInstalacion: number;
  codigoInstalacion: number;
  nombreCliente: string;
  fechaReconexion: string;
  motivo: string;
  observaciones?: string;
  usuarioRegistro: string;
  estado: string;
}

/**
 * DTO para historial completo de cortes y reconexiones
 */
export interface HistorialCortesReconexiones {
  codigoInstalacion: number;
  nombreCliente: string;
  estadoActual: string;
  cortes: Corte[];
  reconexiones: Reconexion[];
}

/**
 * Motivos predefinidos para corte de servicio
 */
export const MOTIVOS_CORTE = [
  'Falta de pago',
  'Deuda acumulada',
  'Uso indebido del servicio',
  'Conexión clandestina',
  'Daño al medidor',
  'Incumplimiento de contrato',
  'Solicitud del cliente',
  'Mantenimiento programado',
  'Orden judicial',
  'Otros'
] as const;

/**
 * Motivos predefinidos para reconexión de servicio
 */
export const MOTIVOS_RECONEXION = [
  'Pago de deuda',
  'Regularización de deuda',
  'Acuerdo de pago',
  'Resolución de incumplimiento',
  'Reparación de daños',
  'Finalización de mantenimiento',
  'Orden judicial',
  'Nuevos términos de contrato',
  'Otros'
] as const;

/**
 * Tipo para motivos de corte
 */
export type MotivoCorte = typeof MOTIVOS_CORTE[number];

/**
 * Tipo para motivos de reconexión
 */
export type MotivoReconexion = typeof MOTIVOS_RECONEXION[number];

/**
 * DTO para plomero
 */
export interface Plomero {
  idPlomero: number;
  nombreCompleto: string;
  dni: string;
  direccion?: string;
  celular?: string;
  estado: string;
}

/**
 * DTO para filtros de corte masivo
 */
export interface FiltrosCorteMasivo {
  zonaInicial: string;
  zonaFinal: string;
  cantidadFacturas: number;
}

/**
 * DTO para instalación con deuda
 */
export interface InstalacionConDeuda {
  idInstalacion: number;
  codigoInstalacion: number;
  nombreCliente: string;
  direccion: string;
  zona: string;
  cantidadFacturasAdeudadas: number;
  montoDeuda: number;
  estado: string;
  seleccionada?: boolean;
}

/**
 * DTO para request de corte masivo
 */
export interface CorteMasivoRequest {
  idPlomero: number;
  idsInstalaciones: number[];
  motivo: string;
  observaciones?: string;
}

/**
 * DTO para response de corte masivo
 */
export interface CorteMasivoResponse {
  totalProcesados: number;
  totalExitosos: number;
  totalFallidos: number;
  cortesRealizados: Corte[];
  errores: string[];
}
