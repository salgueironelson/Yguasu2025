import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '@environments/environment';
import { ApiResponse } from '@shared/models/api-response.model';
import { LoginRequest, LoginResponse, User } from '@shared/models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private apiUrl = `${environment.apiUrl}/auth`;

  // Señales para manejo de estado reactivo
  currentUser = signal<User | null>(this.getUserFromStorage());
  isAuthenticated = signal<boolean>(this.hasToken());

  login(credentials: LoginRequest): Observable<ApiResponse<LoginResponse>> {
    return this.http.post<ApiResponse<LoginResponse>>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap(response => {
          if (response.success && response.data) {
            this.saveTokens(response.data);
            this.saveUser(response.data);
            this.currentUser.set({
              idUsuario: response.data.idUsuario,
              usuario: response.data.usuario,
              tipoUsuario: response.data.tipoUsuario
            });
            this.isAuthenticated.set(true);
          }
        })
      );
  }

  logout(): void {
    localStorage.removeItem(environment.tokenKey);
    localStorage.removeItem(environment.refreshTokenKey);
    localStorage.removeItem(environment.userKey);
    this.currentUser.set(null);
    this.isAuthenticated.set(false);
    this.router.navigate(['/auth/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(environment.tokenKey);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(environment.refreshTokenKey);
  }

  private saveTokens(data: LoginResponse): void {
    localStorage.setItem(environment.tokenKey, data.token);
    localStorage.setItem(environment.refreshTokenKey, data.refreshToken);
  }

  private saveUser(data: LoginResponse): void {
    const user: User = {
      idUsuario: data.idUsuario,
      usuario: data.usuario,
      tipoUsuario: data.tipoUsuario
    };
    localStorage.setItem(environment.userKey, JSON.stringify(user));
  }

  private getUserFromStorage(): User | null {
    const userStr = localStorage.getItem(environment.userKey);
    return userStr ? JSON.parse(userStr) : null;
  }

  private hasToken(): boolean {
    return !!this.getToken();
  }

  validateToken(): Observable<ApiResponse<string>> {
    return this.http.get<ApiResponse<string>>(`${this.apiUrl}/validate`);
  }
}
