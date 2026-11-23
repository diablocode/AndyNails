package mx.udg.cucea.andynails.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.udg.cucea.andynails.ui.viewmodel.CitaUiState
import mx.udg.cucea.andynails.ui.viewmodel.CitaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasScreen(viewModel: CitaViewModel = viewModel(), onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    var fecha by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var idCliente by remember { mutableStateOf("") }
    var idServicio by remember { mutableStateOf("") }
    var idEmpleado by remember { mutableStateOf("") }
    var idPuesto by remember { mutableStateOf("") }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            showErrorDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agenda de Citas") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Atrás") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is CitaUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CitaUiState.Error -> {
                    Text(text = "Error: ${state.message}", modifier = Modifier.align(Alignment.Center))
                }
                is CitaUiState.Success -> {
                    if (state.citas.isEmpty()) {
                        Text(text = "No hay citas programadas", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            items(state.citas) { cita ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = "Fecha: ${cita.fecha} (${cita.hora_inicio} - ${cita.hora_fin})", style = MaterialTheme.typography.titleMedium)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = "Estado: ${cita.estado ?: "Pendiente"} | Precio: \$${cita.precio}")
                                            Text(text = "Cliente ID: ${cita.id_cliente} | Empleado ID: ${cita.id_empleado} | Servicio ID: ${cita.id_servicio}", style = MaterialTheme.typography.bodySmall)
                                        }
                                        TextButton(
                                            onClick = { viewModel.deleteCita(cita.id_cita) },
                                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                        ) {
                                            Text("Cancelar")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Nueva Cita") },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            OutlinedTextField(
                                value = fecha,
                                onValueChange = { fecha = it },
                                label = { Text("Fecha (AAAA-MM-DD) *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = horaInicio,
                                onValueChange = { horaInicio = it },
                                label = { Text("Hora Inicio (HH:MM:SS) *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = horaFin,
                                onValueChange = { horaFin = it },
                                label = { Text("Hora Fin (HH:MM:SS) *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = precio,
                                onValueChange = { precio = it },
                                label = { Text("Precio *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = idCliente,
                                onValueChange = { idCliente = it },
                                label = { Text("ID Cliente *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = idServicio,
                                onValueChange = { idServicio = it },
                                label = { Text("ID Servicio *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = idEmpleado,
                                onValueChange = { idEmpleado = it },
                                label = { Text("ID Empleado *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = idPuesto,
                                onValueChange = { idPuesto = it },
                                label = { Text("ID Puesto (opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            val p = precio.toDoubleOrNull() ?: 0.0
                            val cliId = idCliente.toIntOrNull() ?: 0
                            val srvId = idServicio.toIntOrNull() ?: 0
                            val empId = idEmpleado.toIntOrNull() ?: 0
                            val puesId = idPuesto.toIntOrNull()
                            if (fecha.isNotBlank() && horaInicio.isNotBlank() && horaFin.isNotBlank() && cliId > 0 && srvId > 0 && empId > 0) {
                                viewModel.createCita(fecha, horaInicio, horaFin, p, cliId, srvId, empId, puesId) {
                                    showDialog = false
                                    fecha = ""
                                    horaInicio = ""
                                    horaFin = ""
                                    precio = ""
                                    idCliente = ""
                                    idServicio = ""
                                    idEmpleado = ""
                                    idPuesto = ""
                                }
                            }
                        }) {
                            Text("Guardar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            if (showErrorDialog && errorMessage != null) {
                AlertDialog(
                    onDismissRequest = {
                        showErrorDialog = false
                        viewModel.clearError()
                    },
                    title = { Text("Error") },
                    text = { Text(errorMessage ?: "Error desconocido") },
                    confirmButton = {
                        Button(onClick = {
                            showErrorDialog = false
                            viewModel.clearError()
                        }) {
                            Text("Aceptar")
                        }
                    }
                )
            }
        }
    }
}
