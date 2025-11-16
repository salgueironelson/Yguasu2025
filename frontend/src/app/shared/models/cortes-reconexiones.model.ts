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
