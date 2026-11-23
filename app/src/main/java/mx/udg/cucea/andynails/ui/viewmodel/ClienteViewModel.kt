package mx.udg.cucea.andynails.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.udg.cucea.andynails.data.model.Cliente
import mx.udg.cucea.andynails.data.model.ClienteCreate
import mx.udg.cucea.andynails.data.model.Colonia
import mx.udg.cucea.andynails.data.model.Municipio
import mx.udg.cucea.andynails.data.repository.ClienteRepository
import mx.udg.cucea.andynails.data.network.RetrofitClient

sealed interface ClienteUiState {
    object Loading : ClienteUiState
    data class Success(val clientes: List<Cliente>) : ClienteUiState
    data class Error(val message: String) : ClienteUiState
}

class ClienteViewModel : ViewModel() {
    private val repository = ClienteRepository()
    private val api = RetrofitClient.apiService

    private val _uiState = MutableStateFlow<ClienteUiState>(ClienteUiState.Loading)
    val uiState: StateFlow<ClienteUiState> = _uiState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val municipios = MutableStateFlow<List<Municipio>>(emptyList())
    val colonias = MutableStateFlow<List<Colonia>>(emptyList())

    init {
        fetchClientes()
        fetchCatalogs()
    }

    fun fetchClientes() {
        viewModelScope.launch {
            _uiState.value = ClienteUiState.Loading
            try {
                val list = repository.getClientes()
                _uiState.value = ClienteUiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = ClienteUiState.Error(e.localizedMessage ?: "Error al cargar clientes")
            }
        }
    }

    private fun fetchCatalogs() {
        viewModelScope.launch {
            try {
                municipios.value = api.getMunicipios()
                colonias.value = api.getColonias()
            } catch (_: Exception) {}
        }
    }

    fun createCliente(
        nombre: String,
        apellido: String,
        telefono: String?,
        correo: String?,
        fechaNac: String?,
        calle: String?,
        numero: String?,
        idMunicipio: Int?,
        idColonia: Int?,
        foto: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val nuevo = ClienteCreate(
                    nombre = nombre,
                    apellido = apellido,
                    foto = foto?.ifBlank { null },
                    telefono = telefono?.ifBlank { null },
                    correo = correo?.ifBlank { null },
                    fecha_nac = fechaNac?.ifBlank { null },
                    calle = calle?.ifBlank { null },
                    numero = numero?.ifBlank { null },
                    id_municipio = idMunicipio,
                    id_colonia = idColonia
                )
                repository.createCliente(nuevo)
                fetchClientes()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear cliente: ${e.localizedMessage}"
            }
        }
    }

    fun deleteCliente(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteCliente(id)
                fetchClientes()
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar cliente: ${e.localizedMessage}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
