package mx.udg.cucea.andynails.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.udg.cucea.andynails.data.model.Puesto
import mx.udg.cucea.andynails.data.model.PuestoCreate
import mx.udg.cucea.andynails.data.repository.PuestoRepository

sealed interface PuestoUiState {
    object Loading : PuestoUiState
    data class Success(val puestos: List<Puesto>) : PuestoUiState
    data class Error(val message: String) : PuestoUiState
}

class PuestoViewModel : ViewModel() {
    private val repository = PuestoRepository()

    private val _uiState = MutableStateFlow<PuestoUiState>(PuestoUiState.Loading)
    val uiState: StateFlow<PuestoUiState> = _uiState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        fetchPuestos()
    }

    fun fetchPuestos() {
        viewModelScope.launch {
            _uiState.value = PuestoUiState.Loading
            try {
                val list = repository.getPuestos()
                _uiState.value = PuestoUiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = PuestoUiState.Error(e.localizedMessage ?: "Error al cargar puestos")
            }
        }
    }

    fun createPuesto(nombre: String, usuario: String?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val nuevo = PuestoCreate(nombre = nombre, usuario = usuario)
                repository.createPuesto(nuevo)
                fetchPuestos()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear puesto: ${e.localizedMessage}"
            }
        }
    }

    fun deletePuesto(id: Int) {
        viewModelScope.launch {
            try {
                repository.deletePuesto(id)
                fetchPuestos()
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar puesto: ${e.localizedMessage}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
