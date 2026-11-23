package mx.udg.cucea.andynails.data.model

data class Colonia(
    val id_colonia: Int,
    val nombre: String,
    val cp: String,
    val id_municipio: Int
)

data class ColoniaCreate(
    val nombre: String,
    val cp: String,
    val id_municipio: Int
)
