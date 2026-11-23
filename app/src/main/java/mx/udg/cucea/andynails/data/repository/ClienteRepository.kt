package mx.udg.cucea.andynails.data.repository

import mx.udg.cucea.andynails.data.model.Cliente
import mx.udg.cucea.andynails.data.model.ClienteCreate
import mx.udg.cucea.andynails.data.network.RetrofitClient

class ClienteRepository {
    private val api = RetrofitClient.apiService

    suspend fun getClientes(): List<Cliente> = api.getClientes()
    suspend fun createCliente(cliente: ClienteCreate): Cliente = api.createCliente(cliente)
    suspend fun deleteCliente(id: Int) = api.deleteCliente(id)
}
