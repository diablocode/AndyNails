package mx.udg.cucea.andynails.data.model

data class Especialidad(
    val id_especialidad: Int,
    val nombre: String
)

data class EspecialidadCreate(
    val nombre: String
)
