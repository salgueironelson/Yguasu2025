/**
 * Modelos para el módulo de Altas y Bajas
 */

export interface BajaCreate {
  codigoInstalacion: number;
  motivo: string;
  observaciones?: string;
}

export interface Baja {
  idBaja?: number;
  idInstalacion?: number;
  codigoInstalacion?: number;
  nombreCliente?: string;
  fechaBaja?: Date;
  motivo?: string;
  observaciones?: string;
  usuarioRegistro?: string;
  estado?: string;
}

export interface AltaCreate {
  codigoInstalacion: number;
  motivo: string;
  observaciones?: string;
}

export interface Alta {
  idAlta?: number;
  idInstalacion?: number;
  codigoInstalacion?: number;
  nombreCliente?: string;
  fechaAlta?: Date;
  motivo?: string;
  observaciones?: string;
  usuarioRegistro?: string;
  estado?: string;
}

export interface HistorialAltasBajas {
  codigoInstalacion?: number;
  nombreCliente?: string;
  estadoActual?: string;
  bajas?: Baja[];
  altas?: Alta[];
}

/**
 * Motivos predefinidos para bajas
 */
export const MOTIVOS_BAJA = [
  { value: 'DEUDA', label: 'Deuda Impaga' },
  { value: 'SOLICITUD_CLIENTE', label: 'Solicitud del Cliente' },
  { value: 'FALTA_PAGO', label: 'Falta de Pago' },
  { value: 'CAMBIO_TITULAR', label: 'Cambio de Titular' },
  { value: 'PREDIO_DEMOLIDO', label: 'Predio Demolido/Inhabitable' },
  { value: 'IRREGULARIDAD', label: 'Irregularidad Detectada' },
  { value: 'OTRO', label: 'Otro Motivo' }
];

/**
 * Motivos predefinidos para altas
 */
export const MOTIVOS_ALTA = [
  { value: 'PAGO_DEUDA', label: 'Pago de Deuda' },
  { value: 'SOLICITUD_CLIENTE', label: 'Solicitud del Cliente' },
  { value: 'REGULARIZACION', label: 'Regularización' },
  { value: 'NUEVO_TITULAR', label: 'Nuevo Titular' },
  { value: 'REACTIVACION', label: 'Reactivación de Servicio' },
  { value: 'OTRO', label: 'Otro Motivo' }
];
