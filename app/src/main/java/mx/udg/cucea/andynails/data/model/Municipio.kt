package mx.udg.cucea.andynails.data.model

data class Municipio(
    val id_municipio: Int,
    val nombre: String
)

data class MunicipioCreate(
    val nombre: String
)
