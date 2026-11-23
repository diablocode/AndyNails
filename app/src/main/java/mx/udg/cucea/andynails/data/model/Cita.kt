package mx.udg.cucea.andynails.data.model

data class Cita(
    val id_cita: Int,
    val fecha: String,
    val hora_inicio: String,
    val hora_fin: String,
    val estado: String?,
    val precio: Double,
    val id_cliente: Int,
    val id_servicio: Int,
    val id_empleado: Int,
    val id_puesto: Int?
)

data class CitaCreate(
    val fecha: String,
    val hora_inicio: String,
    val hora_fin: String,
    val estado: String? = "Pendiente",
    val precio: Double,
    val id_cliente: Int,
    val id_servicio: Int,
    val id_empleado: Int,
    val id_puesto: Int? = null
)
