package mx.udg.cucea.andynails.data.repository

import mx.udg.cucea.andynails.data.model.Empleado
import mx.udg.cucea.andynails.data.model.EmpleadoCreate
import mx.udg.cucea.andynails.data.network.RetrofitClient

class EmpleadoRepository {
    private val api = RetrofitClient.apiService

    suspend fun getEmpleados(): List<Empleado> = api.getEmpleados()
    suspend fun createEmpleado(empleado: EmpleadoCreate): Empleado = api.createEmpleado(empleado)
    suspend fun deleteEmpleado(id: Int) = api.deleteEmpleado(id)
}
