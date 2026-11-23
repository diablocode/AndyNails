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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.udg.cucea.andynails.data.model.Servicio
import mx.udg.cucea.andynails.ui.viewmodel.ServicioUiState
import mx.udg.cucea.andynails.ui.viewmodel.ServicioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiciosScreen(
    viewModel: ServicioViewModel = viewModel(),
    onServicioClick: (Servicio) -> Unit,
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
                title = { Text("Gestión de Servicios") },
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
                is ServicioUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ServicioUiState.Error -> {
                    Text(text = "Error: ${state.message}", modifier = Modifier.align(Alignment.Center))
                }
                is ServicioUiState.Success -> {
                    if (state.servicios.isEmpty()) {
                        Text(text = "No hay servicios registrados", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            items(state.servicios) { servicio ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { onServicioClick(servicio) },
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = servicio.nombre_servicio, style = MaterialTheme.typography.titleMedium)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = "Precio: \$${servicio.precio} | Duración: ${servicio.duracion} min")
                                        }
                                        TextButton(
                                            onClick = { viewModel.deleteServicio(servicio.id_servicio) },
                                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                        ) {
                                            Text("Borrar")
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
fun ServicioDetailScreen(servicio: Servicio, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Servicio") },
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
            Text("ID: ${servicio.id_servicio}", style = MaterialTheme.typography.bodyLarge)
            Text("Nombre: ${servicio.nombre_servicio}", style = MaterialTheme.typography.titleLarge)
            Text("Precio: \$${servicio.precio}", style = MaterialTheme.typography.bodyLarge)
            Text("Duración: ${servicio.duracion} minutos", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicioCreateScreen(viewModel: ServicioViewModel = viewModel(), onSaved: () -> Unit, onBack: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Servicio") },
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
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Servicio *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = duracion,
                onValueChange = { duracion = it },
                label = { Text("Duración (minutos) *") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val p = precio.toDoubleOrNull() ?: 0.0
                    val d = duracion.toIntOrNull() ?: 30
                    if (nombre.isNotBlank()) {
                        viewModel.createServicio(nombre, p, d) {
                            onSaved()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Guardar Servicio")
            }
        }
    }
}
