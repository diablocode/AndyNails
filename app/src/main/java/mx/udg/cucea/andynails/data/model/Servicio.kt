package mx.udg.cucea.andynails.data.model

data class Servicio(
    val id_servicio: Int,
    val nombre_servicio: String,
    val precio: Double,
    val duracion: Int,
    val costo: Double?
)

data class ServicioCreate(
    val nombre_servicio: String,
    val precio: Double,
    val duracion: Int,
    val costo: Double?
)
