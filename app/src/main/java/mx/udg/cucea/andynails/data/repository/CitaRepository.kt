package mx.udg.cucea.andynails.data.repository

import mx.udg.cucea.andynails.data.model.Cita
import mx.udg.cucea.andynails.data.model.CitaCreate
import mx.udg.cucea.andynails.data.network.RetrofitClient

class CitaRepository {
    private val api = RetrofitClient.apiService

    suspend fun getCitas(): List<Cita> = api.getCitas()
    suspend fun createCita(cita: CitaCreate): Cita = api.createCita(cita)
    suspend fun deleteCita(id: Int) = api.deleteCita(id)
}
