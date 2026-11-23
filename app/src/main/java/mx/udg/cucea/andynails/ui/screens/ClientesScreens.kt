package mx.udg.cucea.andynails.ui.screens

import android.graphics.Bitmap
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.udg.cucea.andynails.data.model.Cliente
import mx.udg.cucea.andynails.data.model.Colonia
import mx.udg.cucea.andynails.ui.components.ColoniaDropdown
import mx.udg.cucea.andynails.ui.components.showDatePicker
import mx.udg.cucea.andynails.ui.utils.base64ToBitmap
import mx.udg.cucea.andynails.ui.viewmodel.ClienteUiState
import mx.udg.cucea.andynails.ui.viewmodel.ClienteViewModel
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientesScreen(
    viewModel: ClienteViewModel = viewModel(),
    onClientClick: (Cliente) -> Unit,
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
                title = { Text("Gestión de Clientes") },
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
                is ClienteUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ClienteUiState.Error -> {
                    Text(text = "Error: ${state.message}", modifier = Modifier.align(Alignment.Center))
                }
                is ClienteUiState.Success -> {
                    if (state.clientes.isEmpty()) {
                        Text(text = "No hay clientes registrados", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            items(state.clientes) { cliente ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { onClientClick(cliente) },
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = "${cliente.nombre} ${cliente.apellido}", style = MaterialTheme.typography.titleMedium)
                                            if (!cliente.telefono.isNullOrBlank()) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(text = "Tel: ${cliente.telefono}")
                                            }
                                            if (!cliente.correo.isNullOrBlank()) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(text = "Correo: ${cliente.correo}", style = MaterialTheme.typography.bodySmall)
                                            }
                                        }
                                        TextButton(
                                            onClick = { viewModel.deleteCliente(cliente.id_cliente) },
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
fun ClienteDetailScreen(cliente: Cliente, viewModel: ClienteViewModel = viewModel(), onBack: () -> Unit) {
    val colonias by viewModel.colonias.collectAsState()
    val coloniaName = colonias.find { it.id_colonia == cliente.id_colonia }?.nombre ?: cliente.id_colonia?.toString() ?: "No especificada"

    val bitmap = remember(cliente.foto) {
        cliente.foto?.let { base64ToBitmap(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Cliente") },
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
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Foto del cliente",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Text("ID: ${cliente.id_cliente}", style = MaterialTheme.typography.bodyLarge)
            Text("Nombre: ${cliente.nombre} ${cliente.apellido}", style = MaterialTheme.typography.titleLarge)
            Text("Teléfono: ${cliente.telefono ?: "No especificado"}", style = MaterialTheme.typography.bodyLarge)
            Text("Correo: ${cliente.correo ?: "No especificado"}", style = MaterialTheme.typography.bodyLarge)
            Text("Fecha Nacimiento: ${cliente.fecha_nac ?: "No especificada"}", style = MaterialTheme.typography.bodyLarge)
            Text("Calle: ${cliente.calle ?: "No especificada"}", style = MaterialTheme.typography.bodyLarge)
            Text("Número: ${cliente.numero ?: "No especificado"}", style = MaterialTheme.typography.bodyLarge)
            Text("Colonia: $coloniaName", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteCreateScreen(viewModel: ClienteViewModel = viewModel(), onSaved: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val colonias by viewModel.colonias.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var fechaNac by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var selectedColonia by remember { mutableStateOf<Colonia?>(null) }

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var fotoBase64 by remember { mutableStateOf<String?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedBitmap = bitmap
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            fotoBase64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Cliente") },
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
                label = { Text("Nombre *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker(context) { fechaNac = it } }
            ) {
                OutlinedTextField(
                    value = fechaNac,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Fecha Nac. (AAAA-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            OutlinedTextField(
                value = calle,
                onValueChange = { calle = it },
                label = { Text("Calle (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = numero,
                onValueChange = { numero = it },
                label = { Text("Número (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            ColoniaDropdown(
                label = "Colonia (opcional)",
                items = colonias,
                selectedItem = selectedColonia,
                onItemSelected = { selectedColonia = it }
            )

            Button(onClick = { cameraLauncher.launch(null) }, modifier = Modifier.fillMaxWidth()) {
                Text("Tomar Fotografía")
            }

            if (capturedBitmap != null) {
                Image(
                    bitmap = capturedBitmap!!.asImageBitmap(),
                    contentDescription = "Foto del cliente",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }

            Button(
                onClick = {
                    val colId = selectedColonia?.id_colonia
                    val munId = selectedColonia?.id_municipio
                    if (nombre.isNotBlank() && apellido.isNotBlank()) {
                        viewModel.createCliente(nombre, apellido, telefono, correo, fechaNac, calle, numero, munId, colId, fotoBase64) {
                            onSaved()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Guardar Cliente")
            }
        }
    }
}
