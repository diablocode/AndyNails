# Plan de Implementación por Fases - Andy Nails (Android App)

Este plan detalla el proceso paso a paso para desarrollar la aplicación Android de **Andy Nails** conectada a la API REST (`http://3.239.95.124:8000/`), permitiendo validar e ir probando cada módulo de forma incremental.

---

## Fase 1: Configuración del Proyecto y Capa de Red (Retrofit)
**Objetivo:** Establecer la infraestructura base de red, dependencias y modelos de datos en Kotlin.

### Tareas:
1. **Dependencias (`gradle/libs.versions.toml` y `app/build.gradle.kts`):**
   - Agregar Retrofit y OkHttp (`com.squareup.retrofit2:retrofit`, `converter-gson`, `logging-interceptor`).
   - Habilitar Jetpack Compose, ViewModel y Coroutines.
2. **Modelos de Datos (`data/model/`):**
   - Crear clases de datos Kotlin (`Cliente`, `Empleado`, `Cita`, `Servicio`, `Puesto`, `Especialidad`, `Municipio`, `Colonia`) que coincidan con los schemas de la API.
3. **Cliente Retrofit (`data/network/`):**
   - Configurar `RetrofitClient` apuntando a `http://3.239.95.124:8000/`.
   - Definir interfaces de API (`ApiService`) con todos los endpoints (`/clientes`, `/empleados`, `/citas`, `/servicios`, etc.).
4. **Prueba inicial:**
   - Realizar una llamada de prueba (ej. `GET /servicios/`) para verificar la conectividad con el servidor AWS EC2.

---

## Fase 2: Módulo de Catálogos de Soporte (Servicios y Puestos)
**Objetivo:** Permitir la consulta y gestión de servicios de manicura y puestos, necesarios para las citas y empleados.

### Tareas:
1. **Repositorios y ViewModels:**
   - `ServicioRepository` y `ServicioViewModel`.
   - `PuestoRepository` y `PuestoViewModel`.
2. **Interfaz de Usuario (Jetpack Compose):**
   - Pantalla de listado de servicios (precio, duración).
   - Pantalla de creación de servicios.

---

## Fase 3: Módulo de Clientes
**Objetivo:** Gestionar el registro y consulta de clientes del salón.

### Tareas:
1. **API y Repositorio:**
   - Integrar endpoints `/clientes/` (GET, POST, GET /{id}, PUT /{id}, DELETE /{id}).
   - `ClienteViewModel` con manejo de estados (`Loading`, `Success`, `Error`).
2. **UI (Jetpack Compose):**
   - Pantalla de lista de clientes con barra de búsqueda.
   - Formulario de alta de cliente (`nombre`, `apellido`, `telefono`, `correo`, dirección, etc.).

---

## Fase 4: Módulo de Empleados
**Objetivo:** Administrar al personal del negocio y su disponibilidad.

### Tareas:
1. **API y Repositorio:**
   - Integrar endpoints `/empleados/` (GET, POST, GET /{id}, PUT /{id}, DELETE /{id}).
   - `EmpleadoViewModel`.
2. **UI (Jetpack Compose):**
   - Pantalla de lista de empleados con indicador de disponibilidad (`disponibilidad`).
   - Formulario de alta/edición de empleado (vinculado a puestos y especialidades).

---

## Fase 5: Módulo de Citas (Agenda)
**Objetivo:** Gestionar la creación, consulta y estado de las citas.

### Tareas:
1. **API y Repositorio:**
   - Integrar endpoints `/citas/`.
   - `CitaViewModel` manejando la lógica de selección de cliente, servicio y empleado disponible.
2. **UI (Jetpack Compose):**
   - Vista de agenda/calendario de citas.
   - Formulario de nueva cita con selectores desplegables para cliente, servicio y empleado.

---

## Fase 6: Dashboard Principal, Pruebas y Pulido UI
**Objetivo:** Integrar todo en una pantalla principal y realizar pruebas de integración completas.

### Tareas:
1. **Navegación (Navigation Compose):**
   - Conectar Dashboard con los módulos de Clientes, Empleados y Citas.
2. **Pruebas de Integración:**
   - Probar flujos completos contra `http://3.239.95.124:8000/`: crear cliente -> crear empleado -> crear cita validando disponibilidad.
3. **Pulido Final:**
   - Manejo robusto de errores de red (offline/timeout) y diseño Material 3.
