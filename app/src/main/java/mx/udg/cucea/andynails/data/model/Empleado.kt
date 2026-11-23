package mx.udg.cucea.andynails.data.model

data class Empleado(
    val id_empleado: Int,
    val nombre: String,
    val apellido: String,
    val foto: String?,
    val fecha: String?,
    val telefono: String?,
    val calle: String?,
    val numero: String?,
    val disponibilidad: Boolean?,
    val usuario: String,
    val id_municipio: Int?,
    val id_colonia: Int?,
    val id_especialidad: Int?,
    val id_puesto: Int?
)

data class EmpleadoCreate(
    val nombre: String,
    val apellido: String,
    val foto: String?,
    val fecha: String?,
    val telefono: String?,
    val calle: String?,
    val numero: String?,
    val disponibilidad: Boolean? = true,
    val usuario: String,
    val contrasena: String,
    val id_municipio: Int?,
    val id_colonia: Int?,
    val id_especialidad: Int?,
    val id_puesto: Int?
)
