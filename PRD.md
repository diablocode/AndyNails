# Product Requirement Document (PRD) - Andy Nails (Android App)

## 1. Resumen Ejecutivo
**Andy Nails** es una aplicación móvil nativa para Android diseñada para la gestión integral de un negocio de servicios de cuidado de uñas. La aplicación está dirigida exclusivamente al dueño y administradores del negocio, permitiendo optimizar el control de citas, la administración de clientes, empleados, servicios, puestos, especialidades y catálogos geográficos (municipios y colonias). La aplicación se conecta al backend RESTful (desarrollado con FastAPI y PostgreSQL) mediante **Retrofit** utilizando la URL base **`http://3.239.95.124:8000/`**.

---

## 2. Endpoints y Métodos de la API REST (`http://3.239.95.124:8000/`)

A continuación se detallan todos los módulos, endpoints y métodos disponibles en el servidor backend:

### 2.1. Módulo de Citas (`/citas`)
- `GET /citas/` - Listar citas (parámetros opcionales: `skip`, `limit`)
- `POST /citas/` - Crear una nueva cita (Valida existencia de cliente, servicio, empleado y disponibilidad del empleado)
- `GET /citas/{id_cita}` - Obtener detalle de una cita específica por ID
- `PUT /citas/{id_cita}` - Actualizar los datos de una cita existente
- `DELETE /citas/{id_cita}` - Eliminar / cancelar una cita

### 2.2. Módulo de Clientes (`/clientes`)
- `GET /clientes/` - Listar clientes registrados (`skip`, `limit`)
- `POST /clientes/` - Registrar un nuevo cliente (valida duplicidad de correo)
- `GET /clientes/{id_cliente}` - Obtener cliente por ID
- `PUT /clientes/{id_cliente}` - Actualizar información del cliente
- `DELETE /clientes/{id_cliente}` - Eliminar un cliente

### 2.3. Módulo de Empleados (`/empleados`)
- `GET /empleados/` - Listar empleados del salón (`skip`, `limit`)
- `POST /empleados/` - Registrar un nuevo empleado (valida nombre de usuario único, incluye contraseña y relaciones con puesto, especialidad, municipio y colonia)
- `GET /empleados/{id_empleado}` - Obtener empleado por ID
- `PUT /empleados/{id_empleado}` - Actualizar datos del empleado
- `DELETE /empleados/{id_empleado}` - Eliminar empleado

### 2.4. Catálogo de Servicios (`/servicios`)
- `GET /servicios/` - Listar servicios de manicura disponibles (`skip`, `limit`)
- `POST /servicios/` - Crear un nuevo servicio (`nombre_servicio`, `precio`, `duracion` en minutos, `costo`)
- `GET /servicios/{id_servicio}` - Obtener detalle de un servicio por ID
- `PUT /servicios/{id_servicio}` - Actualizar servicio
- `DELETE /servicios/{id_servicio}` - Eliminar servicio

### 2.5. Catálogo de Puestos (`/puestos`)
- `GET /puestos/` - Listar puestos de trabajo (`skip`, `limit`)
- `POST /puestos/` - Crear un nuevo puesto (`nombre`, `usuario`)
- `GET /puestos/{id_puesto}` - Obtener puesto por ID
- `PUT /puestos/{id_puesto}` - Actualizar puesto
- `DELETE /puestos/{id_puesto}` - Eliminar puesto

### 2.6. Catálogo de Especialidades (`/especialidades`)
- `GET /especialidades/` - Listar especialidades (`skip`, `limit`)
- `POST /especialidades/` - Crear nueva especialidad (`nombre`)
- `GET /especialidades/{id_especialidad}` - Obtener especialidad por ID
- `PUT /especialidades/{id_especialidad}` - Actualizar especialidad
- `DELETE /especialidades/{id_especialidad}` - Eliminar especialidad

### 2.7. Catálogo de Municipios (`/municipios`)
- `GET /municipios/` - Listar municipios (`skip`, `limit`)
- `POST /municipios/` - Crear nuevo municipio (`nombre`)
- `GET /municipios/{id_municipio}` - Obtener municipio por ID
- `PUT /municipios/{id_municipio}` - Actualizar municipio
- `DELETE /municipios/{id_municipio}` - Eliminar municipio

### 2.8. Catálogo de Colonias (`/colonias`)
- `GET /colonias/` - Listar colonias (`skip`, `limit`)
- `POST /colonias/` - Crear nueva colonia (valida existencia de `id_municipio`, incluye `nombre` y `cp`)
- `GET /colonias/{id_colonia}` - Obtener colonia por ID
- `PUT /colonias/{id_colonia}` - Actualizar colonia (valida existencia de municipio)
- `DELETE /colonias/{id_colonia}` - Eliminar colonia

---

## 3. Modelos de Datos y Estructuras (Schemas Pydantic)

### Cliente
- `id_cliente` (Int)
- `nombre` (String), `apellido` (String)
- `telefono` (String, opcional), `correo` (EmailStr, opcional)
- `foto` (String, opcional), `fecha_nac` (Date, opcional)
- `calle` (String), `numero` (String), `id_municipio` (Int), `id_colonia` (Int)

### Empleado
- `id_empleado` (Int)
- `nombre` (String), `apellido` (String)
- `usuario` (String), `contrasena` (String)
- `telefono` (String), `disponibilidad` (Boolean)
- `id_puesto` (Int), `id_especialidad` (Int), `id_municipio` (Int), `id_colonia` (Int)

### Cita
- `id_cita` (Int)
- `fecha` (Date), `hora_inicio` (Time), `hora_fin` (Time)
- `estado` (String, por defecto "Pendiente")
- `precio` (Decimal)
- `id_cliente` (Int), `id_servicio` (Int), `id_empleado` (Int), `id_puesto` (Int, opcional)

### Servicio
- `id_servicio` (Int), `nombre_servicio` (String), `precio` (Decimal), `duracion` (Int), `costo` (Decimal, opcional)

### Puesto / Especialidad / Municipio / Colonia
- Estructuras estándar con sus respectivos IDs y campos descriptivos.

---

## 4. Requisitos Funcionales y Alcance de la App Android

1. **Dashboard Principal:** Resumen de citas agendadas y accesos rápidos a gestión.
2. **Gestión de Clientes:** Altas, listados y detalles.
3. **Gestión de Empleados:** Altas de personal, control de disponibilidad y asignación de puestos/especialidades.
4. **Agenda y Citas:** Creación de citas ligadas a clientes, servicios (precio/duración) y empleados disponibles.
5. **Administración de Catálogos:** Consulta y gestión de servicios, puestos, especialidades y ubicaciones geográficas para mantener actualizados los formularios de clientes y empleados.

## 5. Arquitectura Técnica y Retrofit
- **Base URL:** `http://3.239.95.124:8000/`
- **Cliente HTTP:** Retrofit 2 con OkHttp (interceptor de logging).
- **Serialización:** Gson Converter para mapeo automático de JSON a modelos Kotlin con soporte de fechas (`LocalDate`, `LocalTime`) y decimales (`BigDecimal`).
- **Arquitectura:** MVVM con Jetpack Compose y Coroutines.
