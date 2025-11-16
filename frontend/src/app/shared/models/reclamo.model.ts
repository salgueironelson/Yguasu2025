export interface Reclamo {
  id?: number;
  numero?: number;
  codigoInstalacion?: number;

  // Datos del cliente (si tiene instalación)
  nombreCliente?: string;
  numeroMedidor?: string;
  catastro?: string;
  categoria?: string;

  // Datos del reclamo
  celular: string;
  tipoReclamo: string;
  departamento?: string;
  reclamante?: string;
  detalle: string;
  fechaReclamo: Date;
  fechaSolucion?: Date;
  foto?: string;
  estado?: string;
  conclusion?: string;
  ubicacion?: string;
  procedente?: string;

  // Usuarios
  usuarioActual?: number;
  nombreUsuarioActual?: string;
  usuarioRegistro?: number;
  nombreUsuarioRegistro?: string;
  fechaRegistro?: Date;

  // Contadores
  totalPasos?: number;
}

export interface ReclamoCreate {
  codigoInstalacion?: number;
  celular: string;
  tipoReclamo: string;
  fechaReclamo: Date;
  detalle: string;
  reclamante?: string;
  ubicacion?: string;
  departamento?: string;
  foto?: string;
}

export interface ReclamoFilter {
  busqueda?: string;
  tipoReclamo?: string;
  estado?: string;
  departamento?: string;
  procedente?: string;
  fechaDesde?: Date;
  fechaHasta?: Date;
  usuarioActual?: number;
}

export interface Paso {
  id?: number;
  paso?: number;
  detalle?: string;
  tipoPaso?: string;
  comentario?: string;
  usuario?: number;
  nombreUsuario?: string;
  usuarioDestino?: number;
  nombreUsuarioDestino?: string;
  fechaRegistro?: Date;
  fechaInicio?: Date;
  fechaFin?: Date;
  estado?: string;
}

export interface Comentario {
  comentario: string;
}

export interface Transferencia {
  usuarioDestino: number;
  comentario?: string;
}

export interface Conclusion {
  procedente: boolean;
  conclusion: string;
}

export const TIPOS_RECLAMO = [
  { value: 'FALTA_AGUA', label: 'Falta de Agua' },
  { value: 'BAJA_PRESION', label: 'Baja Presión' },
  { value: 'FUGA_AGUA', label: 'Fuga de Agua' },
  { value: 'MEDIDOR_DANADO', label: 'Medidor Dañado' },
  { value: 'FACTURACION', label: 'Facturación' },
  { value: 'ALCANTARILLADO', label: 'Alcantarillado' },
  { value: 'RECONEXION', label: 'Reconexión' },
  { value: 'CORTE_INDEBIDO', label: 'Corte Indebido' },
  { value: 'ATENCION_CLIENTE', label: 'Atención al Cliente' },
  { value: 'OTRO', label: 'Otro' }
];

export const ESTADOS_RECLAMO = [
  { value: 'PENDIENTE', label: 'Pendiente' },
  { value: 'EN_PROCESO', label: 'En Proceso' },
  { value: 'TRANSFERIDO', label: 'Transferido' },
  { value: 'CONCLUIDO', label: 'Concluido' }
];
