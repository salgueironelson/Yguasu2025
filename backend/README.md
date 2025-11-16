# Yguasu Backend - Sistema de Gestión de Servicios Públicos

Backend desarrollado con Spring Boot 3.3.5 para la gestión de servicios públicos de agua y alcantarillado.

## Tecnologías

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Data JPA** - Persistencia de datos
- **Spring Security** - Seguridad y autenticación
- **PostgreSQL** - Base de datos
- **JWT** - Autenticación basada en tokens
- **MapStruct** - Mapeo de entidades a DTOs
- **Lombok** - Reducción de código boilerplate
- **Swagger/OpenAPI** - Documentación de API

## Arquitectura

El proyecto está organizado en módulos funcionales siguiendo principios de Clean Architecture:

```
backend/
├── src/main/java/bo/gob/yguasu/
│   ├── config/                    # Configuraciones generales
│   │   ├── CorsConfig.java
│   │   ├── OpenApiConfig.java
│   │   └── SecurityConfig.java
│   ├── security/                  # Seguridad y JWT
│   │   ├── JwtService.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── CustomUserDetailsService.java
│   ├── common/                    # Componentes compartidos
│   │   ├── domain/               # Entidades base
│   │   ├── dto/                  # DTOs comunes
│   │   ├── exception/            # Excepciones personalizadas
│   │   └── util/                 # Utilidades
│   └── modules/                   # Módulos de negocio
│       ├── auth/                 # Autenticación
│       ├── catastro/             # Catastro técnico
│       ├── clientes/             # Gestión de clientes
│       ├── servicios/            # Servicios comerciales
│       ├── facturacion/          # Facturación
│       ├── lecturas/             # Lecturas de medidores
│       ├── operaciones/          # Cortes, reconexiones
│       └── reclamos/             # Gestión de reclamos
└── src/main/resources/
    ├── application.yml           # Configuración base
    ├── application-dev.yml       # Configuración desarrollo
    └── application-prod.yml      # Configuración producción
```

## Módulos del Sistema

### 1. Autenticación (auth)
- Login con JWT
- Validación de tokens
- Gestión de usuarios del sistema

### 2. Catastro Técnico (catastro)
- Gestión de zonas, rutas, manzanas
- Calles y circuitos
- Medidores y trabajadores

### 3. Clientes (clientes)
- Gestión de usuarios del servicio
- Instalaciones
- Tipos de documentos y usuarios

### 4. Servicios Comerciales (servicios)
- Tipos de servicios
- Categorías
- Descuentos

### 5. Facturación (facturacion)
- Generación de facturas
- Planes de pago
- Detalles de facturación

### 6. Lecturas (lecturas)
- Registro de lecturas
- Tipos de consumo
- Observaciones

### 7. Operaciones (operaciones)
- Cortes y reconexiones
- Altas y bajas de servicios

### 8. Reclamos (reclamos)
- Gestión de reclamos
- Seguimiento de pasos

## Configuración

### Base de Datos

Configurar las variables de entorno o editar `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/yguasu_db
    username: postgres
    password: postgres
```

### JWT

Configurar la clave secreta en variables de entorno:

```bash
export JWT_SECRET=TuClaveSecretaMuySegura
```

## Ejecución

### Con Maven

```bash
# Desarrollo
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Producción
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

### Compilar

```bash
./mvnw clean package
java -jar target/yguasu-backend-1.0.0-SNAPSHOT.jar
```

## Documentación API

Una vez iniciada la aplicación, la documentación Swagger está disponible en:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

## Endpoints Principales

### Autenticación

```
POST /api/auth/login          # Iniciar sesión
GET  /api/auth/validate       # Validar token
```

### Clientes

```
GET    /api/clientes                # Listar clientes
GET    /api/clientes/search?q=...  # Buscar clientes
GET    /api/clientes/{id}           # Obtener cliente
POST   /api/clientes                # Crear cliente
PUT    /api/clientes/{id}           # Actualizar cliente
DELETE /api/clientes/{id}           # Eliminar cliente
```

## Seguridad

- Todos los endpoints requieren autenticación JWT excepto `/auth/**`
- Los tokens tienen una validez de 24 horas
- Los refresh tokens tienen una validez de 7 días
- Las contraseñas se almacenan con BCrypt

## Testing

```bash
# Ejecutar tests
./mvnw test

# Ejecutar tests con coverage
./mvnw test jacoco:report
```

## Variables de Entorno

```bash
DB_URL=jdbc:postgresql://localhost:5432/yguasu_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=TuClaveSecretaMuySegura
```

## Contribución

1. Seguir las convenciones de código Java
2. Escribir tests para nuevas funcionalidades
3. Documentar endpoints con anotaciones Swagger
4. Mantener la arquitectura modular

## Licencia

Propietaria - Gobierno de Bolivia
