# Yguasu Frontend - Sistema de Gestión de Servicios Públicos

Frontend desarrollado con Angular 20 para la gestión de servicios públicos de agua y alcantarillado.

## Tecnologías

- **Angular 20** - Framework principal
- **TypeScript 5.6** - Lenguaje de programación
- **Angular Material** - Componentes UI
- **RxJS** - Programación reactiva
- **NGX-Toastr** - Notificaciones
- **JWT-Decode** - Manejo de tokens JWT

## Arquitectura

El proyecto utiliza una arquitectura modular con componentes standalone:

```
frontend/
├── src/
│   ├── app/
│   │   ├── core/                    # Servicios y funcionalidades core
│   │   │   ├── guards/             # Guards de rutas
│   │   │   ├── interceptors/       # HTTP Interceptors
│   │   │   └── services/           # Servicios principales
│   │   ├── shared/                  # Componentes compartidos
│   │   │   ├── components/         # Componentes reutilizables
│   │   │   ├── directives/         # Directivas personalizadas
│   │   │   ├── pipes/              # Pipes personalizados
│   │   │   └── models/             # Modelos TypeScript
│   │   └── modules/                 # Módulos de negocio
│   │       ├── auth/               # Autenticación
│   │       ├── dashboard/          # Dashboard principal
│   │       ├── clientes/           # Gestión de clientes
│   │       ├── catastro/           # Catastro técnico
│   │       ├── servicios/          # Servicios comerciales
│   │       ├── facturacion/        # Facturación
│   │       ├── lecturas/           # Lecturas
│   │       ├── operaciones/        # Operaciones
│   │       └── reclamos/           # Reclamos
│   ├── assets/                      # Recursos estáticos
│   └── environments/                # Configuraciones de entorno
└── public/                          # Archivos públicos
```

## Características Principales

### 1. Componentes Standalone
- Uso de componentes standalone de Angular 20
- Lazy loading de módulos
- Mejor tree-shaking y rendimiento

### 2. Signals (Señales)
- Manejo de estado reactivo con Angular Signals
- Mayor rendimiento y simplicidad
- Change detection optimizado

### 3. Interceptores HTTP
- **Auth Interceptor**: Agrega token JWT automáticamente
- **Error Interceptor**: Manejo centralizado de errores

### 4. Guards de Ruta
- Protección de rutas autenticadas
- Redirección automática a login

### 5. Angular Material
- UI moderna y responsive
- Componentes consistentes
- Tema personalizable

## Configuración

### Variables de Entorno

**Development** (`src/environments/environment.ts`):
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

**Production** (`src/environments/environment.prod.ts`):
```typescript
export const environment = {
  production: true,
  apiUrl: 'https://api.yguasu.gob.bo/api'
};
```

## Instalación

```bash
# Instalar dependencias
npm install

# Desarrollo
npm start

# La aplicación estará disponible en http://localhost:4200
```

## Scripts Disponibles

```bash
# Servidor de desarrollo
npm start

# Build de producción
npm run build:prod

# Tests
npm test

# Linting
npm run lint
```

## Estructura de Módulos

### Auth (Autenticación)
- Login con JWT
- Almacenamiento de tokens
- Validación de sesión

### Dashboard
- Panel principal
- Navegación entre módulos
- Estadísticas generales

### Clientes
- Lista de clientes
- Búsqueda por CI/NIT/nombre
- CRUD de clientes

### Otros Módulos
- **Catastro**: Gestión de zonas, rutas, calles
- **Servicios**: Tipos de servicios, categorías
- **Facturación**: Generación y gestión de facturas
- **Lecturas**: Registro de consumos
- **Operaciones**: Cortes, reconexiones
- **Reclamos**: Gestión de reclamos

## Servicios HTTP

### AuthService
```typescript
login(credentials: LoginRequest): Observable<ApiResponse<LoginResponse>>
logout(): void
getToken(): string | null
```

### ClienteService
```typescript
findAll(page, size): Observable<ApiResponse<PageResponse<Cliente>>>
search(search, page, size): Observable<ApiResponse<PageResponse<Cliente>>>
findById(id): Observable<ApiResponse<Cliente>>
create(cliente): Observable<ApiResponse<Cliente>>
update(id, cliente): Observable<ApiResponse<Cliente>>
delete(id): Observable<ApiResponse<void>>
```

## Modelos TypeScript

### Cliente
```typescript
interface Cliente {
  idUsuario?: number;
  paterno: string;
  materno: string;
  nombres: string;
  sexo: 'M' | 'F';
  ci?: string;
  nit?: string;
  // ... otros campos
}
```

## Convenciones de Código

- Usar TypeScript strict mode
- Componentes standalone
- Signals para estado reactivo
- Path aliases (@app, @core, @shared, etc.)
- SCSS para estilos
- Material Design

## Build para Producción

```bash
# Build optimizado
npm run build:prod

# Los archivos estarán en dist/yguasu-frontend
```

## Despliegue

El proyecto puede desplegarse en:
- **Nginx**: Servidor web estático
- **Apache**: Con configuración para SPA
- **Netlify/Vercel**: Hosting cloud

### Configuración Nginx (Ejemplo)

```nginx
server {
    listen 80;
    server_name yguasu.gob.bo;
    root /var/www/yguasu-frontend;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

## Testing

```bash
# Tests unitarios
npm test

# Tests con coverage
npm test -- --code-coverage
```

## Contribución

1. Usar componentes standalone
2. Implementar signals para estado
3. Seguir convenciones de código
4. Documentar componentes complejos
5. Escribir tests para nuevas funcionalidades

## Licencia

Propietaria - Gobierno de Bolivia
