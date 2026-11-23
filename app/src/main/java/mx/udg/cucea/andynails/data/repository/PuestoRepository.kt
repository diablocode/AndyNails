package mx.udg.cucea.andynails.data.repository

import mx.udg.cucea.andynails.data.model.Puesto
import mx.udg.cucea.andynails.data.model.PuestoCreate
import mx.udg.cucea.andynails.data.network.RetrofitClient

class PuestoRepository {
    private val api = RetrofitClient.apiService

    suspend fun getPuestos(): List<Puesto> = api.getPuestos()
    suspend fun createPuesto(puesto: PuestoCreate): Puesto = api.createPuesto(puesto)
    suspend fun deletePuesto(id: Int) = api.deletePuesto(id)
}
