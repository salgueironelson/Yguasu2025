export interface LoginRequest {
  usuario: string;
  clave: string;
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
  tipo: string;
  idUsuario: number;
  usuario: string;
  tipoUsuario: string;
}

export interface User {
  idUsuario: number;
  usuario: string;
  tipoUsuario: string;
}
