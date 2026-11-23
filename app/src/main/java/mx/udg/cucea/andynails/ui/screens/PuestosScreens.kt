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
import mx.udg.cucea.andynails.data.model.Puesto
import mx.udg.cucea.andynails.ui.viewmodel.PuestoUiState
import mx.udg.cucea.andynails.ui.viewmodel.PuestoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuestosScreen(
    viewModel: PuestoViewModel = viewModel(),
    onPuestoClick: (Puesto) -> Unit,
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
                title = { Text("Gestión de Puestos") },
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
                is PuestoUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is PuestoUiState.Error -> {
                    Text(text = "Error: ${state.message}", modifier = Modifier.align(Alignment.Center))
                }
                is PuestoUiState.Success -> {
                    if (state.puestos.isEmpty()) {
                        Text(text = "No hay puestos registrados", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            items(state.puestos) { puesto ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { onPuestoClick(puesto) },
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = puesto.nombre, style = MaterialTheme.typography.titleMedium)
                                            if (!puesto.usuario.isNullOrBlank()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(text = "Usuario: ${puesto.usuario}", style = MaterialTheme.typography.bodySmall)
                                            }
                                        }
                                        TextButton(
                                            onClick = { viewModel.deletePuesto(puesto.id_puesto) },
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
fun PuestoDetailScreen(puesto: Puesto, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Puesto") },
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
            Text("ID: ${puesto.id_puesto}", style = MaterialTheme.typography.bodyLarge)
            Text("Nombre: ${puesto.nombre}", style = MaterialTheme.typography.titleLarge)
            Text("Usuario: ${puesto.usuario ?: "No especificado"}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuestoCreateScreen(viewModel: PuestoViewModel = viewModel(), onSaved: () -> Unit, onBack: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Puesto") },
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
                label = { Text("Nombre del Puesto *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (nombre.isNotBlank()) {
                        viewModel.createPuesto(nombre, usuario.ifBlank { null }) {
                            onSaved()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Guardar Puesto")
            }
        }
    }
}
