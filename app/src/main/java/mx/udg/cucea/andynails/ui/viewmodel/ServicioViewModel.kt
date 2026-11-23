package mx.udg.cucea.andynails.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.udg.cucea.andynails.data.model.Servicio
import mx.udg.cucea.andynails.data.model.ServicioCreate
import mx.udg.cucea.andynails.data.repository.ServicioRepository

sealed interface ServicioUiState {
    object Loading : ServicioUiState
    data class Success(val servicios: List<Servicio>) : ServicioUiState
    data class Error(val message: String) : ServicioUiState
}

class ServicioViewModel : ViewModel() {
    private val repository = ServicioRepository()

    private val _uiState = MutableStateFlow<ServicioUiState>(ServicioUiState.Loading)
    val uiState: StateFlow<ServicioUiState> = _uiState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        fetchServicios()
    }

    fun fetchServicios() {
        viewModelScope.launch {
            _uiState.value = ServicioUiState.Loading
            try {
                val list = repository.getServicios()
                _uiState.value = ServicioUiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = ServicioUiState.Error(e.localizedMessage ?: "Error al cargar servicios")
            }
        }
    }

    fun createServicio(nombre: String, precio: Double, duracion: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val nuevo = ServicioCreate(nombre_servicio = nombre, precio = precio, duracion = duracion, costo = null)
                repository.createServicio(nuevo)
                fetchServicios()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear servicio: ${e.localizedMessage}"
            }
        }
    }

    fun deleteServicio(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteServicio(id)
                fetchServicios()
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar servicio: ${e.localizedMessage}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
