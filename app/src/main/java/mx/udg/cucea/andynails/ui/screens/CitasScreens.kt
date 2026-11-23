package mx.udg.cucea.andynails.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.udg.cucea.andynails.data.model.Cita
import mx.udg.cucea.andynails.data.model.Cliente
import mx.udg.cucea.andynails.data.model.Empleado
import mx.udg.cucea.andynails.data.model.Servicio
import mx.udg.cucea.andynails.ui.components.*
import mx.udg.cucea.andynails.ui.viewmodel.CitaUiState
import mx.udg.cucea.andynails.ui.viewmodel.CitaViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasScreen(
    viewModel: CitaViewModel = viewModel(),
    onCitaClick: (Cita) -> Unit,
    onCreateClick: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }

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
            FloatingActionButton(onClick = onCreateClick) {
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
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { onCitaClick(cita) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitaDetailScreen(cita: Cita, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Cita") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Atrás") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("ID Cita: ${cita.id_cita}", style = MaterialTheme.typography.bodyLarge)
            Text("Fecha: ${cita.fecha}", style = MaterialTheme.typography.titleLarge)
            Text("Hora Inicio: ${cita.hora_inicio}", style = MaterialTheme.typography.bodyLarge)
            Text("Hora Fin: ${cita.hora_fin}", style = MaterialTheme.typography.bodyLarge)
            Text("Estado: ${cita.estado ?: "Pendiente"}", style = MaterialTheme.typography.bodyLarge)
            Text("Precio: \$${cita.precio}", style = MaterialTheme.typography.bodyLarge)
            Text("Cliente ID: ${cita.id_cliente}", style = MaterialTheme.typography.bodyLarge)
            Text("Servicio ID: ${cita.id_servicio}", style = MaterialTheme.typography.bodyLarge)
            Text("Empleado ID: ${cita.id_empleado}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitaCreateScreen(viewModel: CitaViewModel = viewModel(), onSaved: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val clientes by viewModel.clientes.collectAsState()
    val servicios by viewModel.servicios.collectAsState()
    val empleados by viewModel.empleados.collectAsState()

    var fecha by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    var selectedCliente by remember { mutableStateOf<Cliente?>(null) }
    var selectedServicio by remember { mutableStateOf<Servicio?>(null) }
    var selectedEmpleado by remember { mutableStateOf<Empleado?>(null) }

    LaunchedEffect(horaInicio, selectedServicio) {
        val servicio = selectedServicio
        if (servicio != null && horaInicio.isNotBlank()) {
            precio = servicio.precio.toString()
            try {
                val parts = horaInicio.split(":")
                if (parts.size >= 2) {
                    val h = parts[0].toInt()
                    val m = parts[1].toInt()
                    val totalMinutes = h * 60 + m + servicio.duracion
                    val newH = (totalMinutes / 60) % 24
                    val newM = totalMinutes % 60
                    horaFin = String.format(Locale.getDefault(), "%02d:%02d:00", newH, newM)
                }
            } catch (_: Exception) {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Cita") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Atrás") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker(context) { fecha = it } }
            ) {
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Fecha (AAAA-MM-DD) *") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTimePicker(context) { horaInicio = it } }
            ) {
                OutlinedTextField(
                    value = horaInicio,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Hora Inicio *") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            OutlinedTextField(
                value = horaFin,
                onValueChange = {},
                readOnly = true,
                label = { Text("Hora Fin (Autocalculada)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = precio,
                onValueChange = {},
                readOnly = true,
                label = { Text("Precio (Autocalculado)") },
                modifier = Modifier.fillMaxWidth()
            )
            ClienteDropdown(
                label = "Cliente *",
                items = clientes,
                selectedItem = selectedCliente,
                onItemSelected = { selectedCliente = it }
            )
            ServicioDropdown(
                label = "Servicio *",
                items = servicios,
                selectedItem = selectedServicio,
                onItemSelected = { selectedServicio = it }
            )
            EmpleadoDropdown(
                label = "Empleado *",
                items = empleados,
                selectedItem = selectedEmpleado,
                onItemSelected = { selectedEmpleado = it }
            )

            Button(
                onClick = {
                    val p = precio.toDoubleOrNull() ?: 0.0
                    val cliId = selectedCliente?.id_cliente ?: 0
                    val srvId = selectedServicio?.id_servicio ?: 0
                    val empId = selectedEmpleado?.id_empleado ?: 0
                    if (fecha.isNotBlank() && horaInicio.isNotBlank() && horaFin.isNotBlank() && cliId > 0 && srvId > 0 && empId > 0) {
                        viewModel.createCita(fecha, horaInicio, horaFin, p, cliId, srvId, empId, null) {
                            onSaved()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Guardar Cita")
            }
        }
    }
}
