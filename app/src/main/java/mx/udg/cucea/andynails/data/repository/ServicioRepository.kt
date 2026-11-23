package mx.udg.cucea.andynails.data.repository

import mx.udg.cucea.andynails.data.model.Servicio
import mx.udg.cucea.andynails.data.model.ServicioCreate
import mx.udg.cucea.andynails.data.network.RetrofitClient

class ServicioRepository {
    private val api = RetrofitClient.apiService

    suspend fun getServicios(): List<Servicio> = api.getServicios()
    suspend fun createServicio(servicio: ServicioCreate): Servicio = api.createServicio(servicio)
    suspend fun deleteServicio(id: Int) = api.deleteServicio(id)
}
