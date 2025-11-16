# Yguasu - Sistema de Gestión de Servicios Públicos

Sistema integral para la gestión de servicios públicos de agua y alcantarillado, desarrollado con tecnologías modernas y escalables.

## Descripción

Yguasu es una solución completa que permite gestionar todos los aspectos relacionados con la prestación de servicios públicos de agua y alcantarillado, incluyendo:

- **Gestión de Clientes**: Registro y administración de usuarios del servicio
- **Catastro Técnico**: Control de zonas, rutas, manzanas y calles
- **Servicios Comerciales**: Tipos de servicios, categorías y descuentos
- **Facturación**: Generación y gestión de facturas con planes de pago
- **Lecturas**: Registro de consumos y medidores
- **Operaciones**: Cortes, reconexiones, altas y bajas
- **Reclamos**: Sistema de gestión de reclamos y seguimiento

## Stack Tecnológico

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** con JWT
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **MapStruct** (Mapeo de entidades)
- **Lombok** (Reducción de boilerplate)
- **Swagger/OpenAPI** (Documentación)

### Frontend
- **Angular 20**
- **TypeScript 5.6**
- **Angular Material**
- **RxJS**
- **Signals** (Estado reactivo)
- **NGX-Toastr** (Notificaciones)

## Estructura del Proyecto

```
Yguasu2025/
├── backend/                 # Aplicación Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── bo/gob/yguasu/
│   │   │   │       ├── config/           # Configuraciones
│   │   │   │       ├── security/         # Seguridad JWT
│   │   │   │       ├── common/           # Componentes comunes
│   │   │   │       └── modules/          # Módulos de negocio
│   │   │   │           ├── auth/
│   │   │   │           ├── catastro/
│   │   │   │           ├── clientes/
│   │   │   │           ├── servicios/
│   │   │   │           ├── facturacion/
│   │   │   │           ├── lecturas/
│   │   │   │           ├── operaciones/
│   │   │   │           └── reclamos/
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
│
└── frontend/                # Aplicación Angular
    ├── src/
    │   ├── app/
    │   │   ├── core/             # Servicios core
    │   │   ├── shared/           # Componentes compartidos
    │   │   └── modules/          # Módulos de negocio
    │   │       ├── auth/
    │   │       ├── dashboard/
    │   │       ├── clientes/
    │   │       ├── catastro/
    │   │       ├── servicios/
    │   │       ├── facturacion/
    │   │       ├── lecturas/
    │   │       ├── operaciones/
    │   │       └── reclamos/
    │   ├── assets/
    │   └── environments/
    └── package.json
```

## Módulos del Sistema

### 1. Autenticación y Seguridad
- Login con JWT
- Control de acceso basado en roles
- Tokens con expiración configurable

### 2. Gestión de Clientes
- Registro de usuarios del servicio
- Búsqueda por CI, NIT o nombre
- Historial de instalaciones

### 3. Catastro Técnico
- Organización por zonas y rutas
- Gestión de manzanas y calles
- Control de medidores
- Asignación de trabajadores

### 4. Servicios Comerciales
- Tipos de servicios (agua, alcantarillado)
- Categorías de usuarios
- Sistema de descuentos

### 5. Facturación
- Generación automática de facturas
- Planes de pago personalizados
- Integración con sistema de impuestos
- Control de pagos y mora

### 6. Lecturas
- Registro de consumos
- Tipos de observaciones
- Asignación de encargados por zona
- Historial de lecturas

### 7. Operaciones
- Programación de cortes
- Gestión de reconexiones
- Altas y bajas de servicios
- Seguimiento de plomeros

### 8. Reclamos
- Sistema de tickets
- Asignación por departamentos
- Seguimiento de resolución
- Registro fotográfico

## Requisitos Previos

### Backend
- Java JDK 17 o superior
- Maven 3.8+
- PostgreSQL 13+

### Frontend
- Node.js 18+ y npm
- Angular CLI 20

## Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/your-org/Yguasu2025.git
cd Yguasu2025
```

### 2. Configurar Base de Datos

```sql
-- Crear base de datos
CREATE DATABASE yguasu_db;

-- Las tablas ya están creadas según el esquema proporcionado
```

### 3. Configurar Backend

```bash
cd backend

# Editar application.yml con sus credenciales de BD
# Configurar variables de entorno
export DB_USERNAME=postgres
export DB_PASSWORD=tu_password
export JWT_SECRET=tu_secret_key

# Compilar y ejecutar
./mvnw clean install
./mvnw spring-boot:run
```

El backend estará disponible en: `http://localhost:8080/api`

### 4. Configurar Frontend

```bash
cd frontend

# Instalar dependencias
npm install

# Ejecutar en desarrollo
npm start
```

El frontend estará disponible en: `http://localhost:4200`

## Documentación API

Una vez iniciado el backend, la documentación Swagger está disponible en:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

## Endpoints Principales

### Autenticación
```
POST /api/auth/login          # Login
GET  /api/auth/validate       # Validar token
```

### Clientes
```
GET    /api/clientes                # Listar
GET    /api/clientes/search?q=...  # Buscar
GET    /api/clientes/{id}           # Obtener
POST   /api/clientes                # Crear
PUT    /api/clientes/{id}           # Actualizar
DELETE /api/clientes/{id}           # Eliminar
```

## Seguridad

- **Autenticación**: JWT (JSON Web Tokens)
- **Contraseñas**: Encriptadas con BCrypt
- **Autorización**: Basada en roles
- **CORS**: Configurado para permitir frontend
- **HTTPS**: Recomendado para producción

## Testing

### Backend
```bash
cd backend
./mvnw test
```

### Frontend
```bash
cd frontend
npm test
```

## Despliegue en Producción

### Backend

```bash
# Build
./mvnw clean package -DskipTests

# Ejecutar JAR
java -jar target/yguasu-backend-1.0.0-SNAPSHOT.jar --spring.profiles.active=prod
```

### Frontend

```bash
# Build de producción
npm run build:prod

# Los archivos estarán en dist/yguasu-frontend
# Desplegar en servidor web (Nginx, Apache, etc.)
```

## Variables de Entorno

### Backend
```bash
DB_URL=jdbc:postgresql://localhost:5432/yguasu_db
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_secret_key
```

### Frontend
Configurar en `src/environments/environment.prod.ts`

## Contribución

1. Crear una rama para la nueva funcionalidad
2. Seguir las convenciones de código
3. Escribir tests
4. Crear Pull Request

## Arquitectura

El proyecto sigue principios de:

- **Clean Architecture**
- **Domain-Driven Design (DDD)**
- **SOLID**
- **RESTful API**
- **Reactive Programming** (Frontend)

## Licencia

Propietaria - Gobierno de Bolivia

## Soporte

Para soporte técnico contactar a:
- Email: soporte@yguasu.gob.bo
- Teléfono: +591 XXX XXXXX

---

**Desarrollado por el Equipo de Desarrollo de Yguasu**
