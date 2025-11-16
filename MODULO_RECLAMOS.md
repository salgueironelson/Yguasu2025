# Módulo de Atención a Reclamos - Yguasu

## 📋 Descripción

Módulo completo para la gestión de reclamos de clientes del sistema Yguasu. Permite registrar, dar seguimiento y concluir reclamos de clientes con o sin instalación registrada.

## ✨ Funcionalidades Implementadas

### 1. Creación de Reclamos

**Dos modalidades:**

#### a) Cliente con instalación registrada
- Búsqueda por código de instalación
- Autocompletado de datos del cliente:
  - Nombre del cliente
  - Número de medidor
  - Catastro
  - Categoría
- Campos obligatorios adicionales:
  - Celular del reclamante
  - Tipo de reclamo (lista desplegable)
  - Fecha del reclamo (por defecto: hoy, editable)
  - Detalle del reclamo

#### b) Cliente sin instalación (reclamo anónimo)
- Campos obligatorios:
  - Nombre del reclamante
  - Celular
  - Tipo de reclamo
  - Fecha
  - Detalle
- Genera ID único automáticamente

### 2. Listado de Reclamos

**Filtros disponibles:**
- Código / Nombre / Medidor (búsqueda general)
- Tipo de reclamo
- Fecha (rango desde-hasta)
- Estado:
  - Pendiente
  - En Proceso
  - Transferido
  - Concluido
- Área/Departamento asignada
- Procedencia (Procedente / No procedente)

### 3. Acciones sobre Reclamos

#### a) Imprimir Ficha de Reclamo
- Genera PDF con información completa del reclamo
- Incluye datos del cliente e historial

#### b) Imprimir Ficha de Trabajo
- Documento para personal técnico (plomero)
- Incluye espacio para observaciones

#### c) Registro de Comentarios/Avances
- Solo usuario asignado puede comentar
- Historial de todos los comentarios
- Cambio automático de estado PENDIENTE → EN_PROCESO

#### d) Transferencia a Otra Área
- Selección de usuario/área destino
- Comentario de transferencia (opcional pero recomendado)
- Historial de transferencias
- Cambio de responsable automático

#### e) Conclusión del Reclamo
- Campos obligatorios:
  - ¿Procedente? (Sí/No)
  - Explicación de conclusión (mínimo 20 caracteres)
- Estado → CONCLUIDO
- Fecha de cierre automática
- Registro en historial

## 🏗️ Arquitectura Técnica

### Backend (Spring Boot)

```
modules/reclamos/
├── domain/
│   ├── Reclamo.java          # Entidad principal
│   ├── Paso.java              # Historial de acciones
│   ├── TUsuario.java          # Usuarios del sistema
│   └── TipoReclamo.java       # Enum de tipos
├── dto/
│   ├── ReclamoDTO.java
│   ├── ReclamoCreateDTO.java
│   ├── ReclamoFilterDTO.java
│   ├── ComentarioDTO.java
│   ├── TransferenciaDTO.java
│   ├── ConclusionDTO.java
│   └── PasoDTO.java
├── repository/
│   ├── ReclamoRepository.java  # Queries con filtros
│   ├── PasoRepository.java
│   └── TUsuarioRepository.java
├── service/
│   └── ReclamoService.java     # Lógica de negocio
└── controller/
    └── ReclamoController.java  # Endpoints REST
```

### Frontend (Angular 20)

```
modules/reclamos/
├── reclamo-create/
│   ├── reclamo-create.component.ts
│   ├── reclamo-create.component.html
│   └── reclamo-create.component.scss
├── reclamos-list/              # TODO: Implementar
├── reclamo-detail/             # TODO: Implementar
└── reclamos.routes.ts
```

## 🔌 API Endpoints

### Base URL: `/api/reclamos`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/` | Crear nuevo reclamo |
| GET | `/` | Listar reclamos con filtros |
| GET | `/{id}` | Obtener reclamo por ID |
| GET | `/{id}/historial` | Obtener historial de pasos |
| POST | `/{id}/comentario` | Agregar comentario |
| POST | `/{id}/transferir` | Transferir a otro usuario |
| POST | `/{id}/concluir` | Concluir reclamo |

### Ejemplos de Uso

#### Crear Reclamo (con instalación)
```json
POST /api/reclamos
{
  "codigoInstalacion": 12345,
  "celular": "70123456",
  "tipoReclamo": "FALTA_AGUA",
  "fechaReclamo": "2024-01-15T10:30:00",
  "detalle": "Falta de agua desde hace 3 días",
  "reclamante": "Juan Pérez"
}
```

#### Crear Reclamo (sin instalación)
```json
POST /api/reclamos
{
  "celular": "70123456",
  "tipoReclamo": "OTRO",
  "fechaReclamo": "2024-01-15T10:30:00",
  "detalle": "Consulta sobre servicio",
  "reclamante": "María García",
  "ubicacion": "Av. Principal #123"
}
```

#### Agregar Comentario
```json
POST /api/reclamos/1/comentario
{
  "comentario": "Se realizó inspección in situ. Problema identificado."
}
```

#### Transferir Reclamo
```json
POST /api/reclamos/1/transferir
{
  "usuarioDestino": 5,
  "comentario": "Transferido a área técnica para revisión"
}
```

#### Concluir Reclamo
```json
POST /api/reclamos/1/concluir
{
  "procedente": true,
  "conclusion": "Reclamo atendido satisfactoriamente. Se reparó la tubería dañada."
}
```

## 📊 Modelo de Datos

### Tabla: t_reclamo
- `id`: ID único del reclamo
- `numero`: Número correlativo
- `codigo_instalacion`: Código del cliente (nullable)
- `celular`: Teléfono del reclamante
- `tipo_reclamo`: Tipo de reclamo
- `reclamante`: Nombre del reclamante
- `detalle`: Descripción del problema
- `fecha_reclamo`: Fecha del reclamo
- `fecha_solucion`: Fecha de cierre
- `estado`: PENDIENTE | EN_PROCESO | TRANSFERIDO | CONCLUIDO
- `procedente`: SI | NO | null
- `conclusion`: Texto de conclusión
- `usuario_actual`: Usuario responsable
- `usuario_registro`: Usuario que creó el reclamo

### Tabla: t_paso
Historial de acciones:
- `id`: ID del paso
- `id_reclamo`: Referencia al reclamo
- `paso`: Número secuencial
- `tipo_paso`: CREACION | COMENTARIO | TRANSFERENCIA | CONCLUSION
- `detalle`: Descripción breve
- `comentario`: Comentario detallado
- `usuario`: Usuario que ejecutó la acción
- `usuario_destino`: Usuario destino (en transferencias)
- `fecha_registro`: Timestamp de la acción

## 🔧 Pendientes de Implementación

### Frontend
- [ ] Componente de listado con filtros (`reclamos-list.component`)
- [ ] Componente de detalle con historial (`reclamo-detail.component`)
- [ ] Diálogos para comentarios, transferencia y conclusión
- [ ] Generación de PDFs (fichas de reclamo y trabajo)
- [ ] Integración real con API de instalaciones para autocompletar datos

### Backend
- [ ] Integración con módulo de instalaciones/clientes
- [ ] Generación de PDFs con JasperReports o similar
- [ ] Notificaciones por email/SMS
- [ ] Carga de fotos del reclamo
- [ ] Estadísticas y dashboard de reclamos

## 🚀 Cómo Usar

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

API disponible en: http://localhost:8080/api
Swagger: http://localhost:8080/api/swagger-ui.html

### Frontend
```bash
cd frontend
npm install
npm start
```

Aplicación disponible en: http://localhost:4200

### Navegación
1. Login: `/auth/login`
2. Dashboard: `/dashboard`
3. Reclamos: `/dashboard/reclamos`
4. Crear Reclamo: `/dashboard/reclamos/crear`

## 📝 Notas Importantes

- Los reclamos se numeran automáticamente de forma correlativa
- El estado inicial siempre es "PENDIENTE"
- Solo el usuario asignado puede comentar, transferir o concluir
- El historial completo queda registrado en la tabla `t_paso`
- La fecha de reclamo es editable pero por defecto es la actual
- Los reclamos sin instalación no tienen código de cliente (es null)

## 🎨 Tipos de Reclamo Disponibles

- Falta de Agua
- Baja Presión
- Fuga de Agua
- Medidor Dañado
- Facturación
- Alcantarillado
- Reconexión
- Corte Indebido
- Atención al Cliente
- Otro

## 👥 Roles y Permisos

- **Todos los usuarios autenticados** pueden crear reclamos
- **Usuario asignado** puede:
  - Agregar comentarios
  - Transferir a otro usuario
  - Concluir el reclamo
- **Supervisores** pueden ver todos los reclamos
- **Administradores** tienen acceso completo

---

**Desarrollado para Sistema Yguasu - 2024**
