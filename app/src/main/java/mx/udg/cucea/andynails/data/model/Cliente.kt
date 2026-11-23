package mx.udg.cucea.andynails.data.model

data class Cliente(
    val id_cliente: Int,
    val nombre: String,
    val apellido: String,
    val foto: String?,
    val telefono: String?,
    val correo: String?,
    val fecha_nac: String?,
    val calle: String?,
    val numero: String?,
    val id_municipio: Int?,
    val id_colonia: Int?
)

data class ClienteCreate(
    val nombre: String,
    val apellido: String,
    val foto: String?,
    val telefono: String?,
    val correo: String?,
    val fecha_nac: String?,
    val calle: String?,
    val numero: String?,
    val id_municipio: Int?,
    val id_colonia: Int?
)
