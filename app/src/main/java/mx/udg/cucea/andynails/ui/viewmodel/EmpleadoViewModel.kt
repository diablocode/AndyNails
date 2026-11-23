package mx.udg.cucea.andynails.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.udg.cucea.andynails.data.model.Colonia
import mx.udg.cucea.andynails.data.model.Empleado
import mx.udg.cucea.andynails.data.model.EmpleadoCreate
import mx.udg.cucea.andynails.data.model.Especialidad
import mx.udg.cucea.andynails.data.model.Municipio
import mx.udg.cucea.andynails.data.model.Puesto
import mx.udg.cucea.andynails.data.repository.EmpleadoRepository
import mx.udg.cucea.andynails.data.network.RetrofitClient

sealed interface EmpleadoUiState {
    object Loading : EmpleadoUiState
    data class Success(val empleados: List<Empleado>) : EmpleadoUiState
    data class Error(val message: String) : EmpleadoUiState
}

class EmpleadoViewModel : ViewModel() {
    private val repository = EmpleadoRepository()
    private val api = RetrofitClient.apiService

    private val _uiState = MutableStateFlow<EmpleadoUiState>(EmpleadoUiState.Loading)
    val uiState: StateFlow<EmpleadoUiState> = _uiState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val municipios = MutableStateFlow<List<Municipio>>(emptyList())
    val colonias = MutableStateFlow<List<Colonia>>(emptyList())
    val especialidades = MutableStateFlow<List<Especialidad>>(emptyList())
    val puestos = MutableStateFlow<List<Puesto>>(emptyList())

    init {
        fetchEmpleados()
        fetchCatalogs()
    }

    fun fetchEmpleados() {
        viewModelScope.launch {
            _uiState.value = EmpleadoUiState.Loading
            try {
                val list = repository.getEmpleados()
                _uiState.value = EmpleadoUiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = EmpleadoUiState.Error(e.localizedMessage ?: "Error al cargar empleados")
            }
        }
    }

    private fun fetchCatalogs() {
        viewModelScope.launch {
            try {
                municipios.value = api.getMunicipios()
                colonias.value = api.getColonias()
                especialidades.value = api.getEspecialidades()
                puestos.value = api.getPuestos()
            } catch (_: Exception) {}
        }
    }

    fun createEmpleado(
        nombre: String,
        apellido: String,
        usuario: String,
        contrasena: String,
        telefono: String?,
        fecha: String?,
        calle: String?,
        numero: String?,
        disponibilidad: Boolean,
        idMunicipio: Int?,
        idColonia: Int?,
        idEspecialidad: Int?,
        idPuesto: Int?,
        foto: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val nuevo = EmpleadoCreate(
                    nombre = nombre,
                    apellido = apellido,
                    usuario = usuario,
                    contrasena = contrasena,
                    foto = foto?.ifBlank { null },
                    fecha = fecha?.ifBlank { null },
                    telefono = telefono?.ifBlank { null },
                    calle = calle?.ifBlank { null },
                    numero = numero?.ifBlank { null },
                    disponibilidad = disponibilidad,
                    id_municipio = idMunicipio,
                    id_colonia = idColonia,
                    id_especialidad = idEspecialidad,
                    id_puesto = idPuesto
                )
                repository.createEmpleado(nuevo)
                fetchEmpleados()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear empleado: ${e.localizedMessage}"
            }
        }
    }

    fun deleteEmpleado(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteEmpleado(id)
                fetchEmpleados()
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar empleado: ${e.localizedMessage}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
