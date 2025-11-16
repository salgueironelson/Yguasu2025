import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const toastr = inject(ToastrService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'Ha ocurrido un error';

      if (error.error instanceof ErrorEvent) {
        // Error del lado del cliente
        errorMessage = `Error: ${error.error.message}`;
      } else {
        // Error del lado del servidor
        switch (error.status) {
          case 401:
            errorMessage = 'No autorizado. Por favor, inicie sesión nuevamente.';
            localStorage.clear();
            router.navigate(['/auth/login']);
            break;
          case 403:
            errorMessage = 'No tiene permisos para realizar esta acción.';
            break;
          case 404:
            errorMessage = error.error?.message || 'Recurso no encontrado.';
            break;
          case 400:
            errorMessage = error.error?.message || 'Solicitud inválida.';
            if (error.error?.validationErrors) {
              const validationErrors = error.error.validationErrors;
              errorMessage = Object.values(validationErrors).join(', ');
            }
            break;
          case 500:
            errorMessage = 'Error interno del servidor. Por favor, intente más tarde.';
            break;
          default:
            errorMessage = error.error?.message || `Error: ${error.status}`;
        }
      }

      toastr.error(errorMessage, 'Error');
      return throwError(() => error);
    })
  );
};
