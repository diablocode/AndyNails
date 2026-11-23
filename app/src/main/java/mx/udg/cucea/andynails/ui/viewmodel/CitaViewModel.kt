package mx.udg.cucea.andynails.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.udg.cucea.andynails.data.model.Cita
import mx.udg.cucea.andynails.data.model.CitaCreate
import mx.udg.cucea.andynails.data.model.Cliente
import mx.udg.cucea.andynails.data.model.Empleado
import mx.udg.cucea.andynails.data.model.Puesto
import mx.udg.cucea.andynails.data.model.Servicio
import mx.udg.cucea.andynails.data.repository.CitaRepository
import mx.udg.cucea.andynails.data.network.RetrofitClient

sealed interface CitaUiState {
    object Loading : CitaUiState
    data class Success(val citas: List<Cita>) : CitaUiState
    data class Error(val message: String) : CitaUiState
}

class CitaViewModel : ViewModel() {
    private val repository = CitaRepository()
    private val api = RetrofitClient.apiService

    private val _uiState = MutableStateFlow<CitaUiState>(CitaUiState.Loading)
    val uiState: StateFlow<CitaUiState> = _uiState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val clientes = MutableStateFlow<List<Cliente>>(emptyList())
    val servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val empleados = MutableStateFlow<List<Empleado>>(emptyList())
    val puestos = MutableStateFlow<List<Puesto>>(emptyList())

    init {
        fetchCitas()
        fetchCatalogs()
    }

    fun fetchCitas() {
        viewModelScope.launch {
            _uiState.value = CitaUiState.Loading
            try {
                val list = repository.getCitas()
                _uiState.value = CitaUiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = CitaUiState.Error(e.localizedMessage ?: "Error al cargar citas")
            }
        }
    }

    private fun fetchCatalogs() {
        viewModelScope.launch {
            try {
                clientes.value = api.getClientes()
                servicios.value = api.getServicios()
                empleados.value = api.getEmpleados()
                puestos.value = api.getPuestos()
            } catch (_: Exception) {}
        }
    }

    fun createCita(
        fecha: String,
        horaInicio: String,
        horaFin: String,
        precio: Double,
        idCliente: Int,
        idServicio: Int,
        idEmpleado: Int,
        idPuesto: Int?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val nuevo = CitaCreate(
                    fecha = fecha,
                    hora_inicio = horaInicio,
                    hora_fin = horaFin,
                    estado = "Pendiente",
                    precio = precio,
                    id_cliente = idCliente,
                    id_servicio = idServicio,
                    id_empleado = idEmpleado,
                    id_puesto = idPuesto
                )
                repository.createCita(nuevo)
                fetchCitas()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear cita: ${e.localizedMessage}"
            }
        }
    }

    fun deleteCita(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteCita(id)
                fetchCitas()
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar cita: ${e.localizedMessage}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
