package mx.udg.cucea.andynails.data.model

data class Puesto(
    val id_puesto: Int,
    val nombre: String,
    val usuario: String?
)

data class PuestoCreate(
    val nombre: String,
    val usuario: String? = null
)
