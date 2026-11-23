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
import mx.udg.cucea.andynails.data.model.Colonia
import mx.udg.cucea.andynails.data.model.Empleado
import mx.udg.cucea.andynails.data.model.Especialidad
import mx.udg.cucea.andynails.data.model.Puesto
import mx.udg.cucea.andynails.ui.components.*
import mx.udg.cucea.andynails.ui.utils.base64ToBitmap
import mx.udg.cucea.andynails.ui.viewmodel.EmpleadoUiState
import mx.udg.cucea.andynails.ui.viewmodel.EmpleadoViewModel
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadosScreen(
    viewModel: EmpleadoViewModel = viewModel(),
    onEmpleadoClick: (Empleado) -> Unit,
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
                title = { Text("Gestión de Empleados") },
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
                is EmpleadoUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is EmpleadoUiState.Error -> {
                    Text(text = "Error: ${state.message}", modifier = Modifier.align(Alignment.Center))
                }
                is EmpleadoUiState.Success -> {
                    if (state.empleados.isEmpty()) {
                        Text(text = "No hay empleados registrados", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            items(state.empleados) { empleado ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { onEmpleadoClick(empleado) },
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = "${empleado.nombre} ${empleado.apellido}", style = MaterialTheme.typography.titleMedium)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = "Usuario: ${empleado.usuario}", style = MaterialTheme.typography.bodySmall)
                                            if (!empleado.telefono.isNullOrBlank()) {
                                                Text(text = "Tel: ${empleado.telefono}", style = MaterialTheme.typography.bodySmall)
                                            }
                                            Text(text = "Disponible: ${if (empleado.disponibilidad == true) "Sí" else "No"}", style = MaterialTheme.typography.bodySmall)
                                        }
                                        TextButton(
                                            onClick = { viewModel.deleteEmpleado(empleado.id_empleado) },
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
fun EmpleadoDetailScreen(empleado: Empleado, viewModel: EmpleadoViewModel = viewModel(), onBack: () -> Unit) {
    val colonias by viewModel.colonias.collectAsState()
    val especialidades by viewModel.especialidades.collectAsState()
    val puestos by viewModel.puestos.collectAsState()

    val coloniaName = colonias.find { it.id_colonia == empleado.id_colonia }?.nombre ?: empleado.id_colonia?.toString() ?: "No especificada"
    val especialidadName = especialidades.find { it.id_especialidad == empleado.id_especialidad }?.nombre ?: empleado.id_especialidad?.toString() ?: "No especificada"
    val puestoName = puestos.find { it.id_puesto == empleado.id_puesto }?.nombre ?: empleado.id_puesto?.toString() ?: "No especificado"

    val bitmap = remember(empleado.foto) {
        empleado.foto?.let { base64ToBitmap(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Empleado") },
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
                    contentDescription = "Foto del empleado",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Text("ID: ${empleado.id_empleado}", style = MaterialTheme.typography.bodyLarge)
            Text("Nombre: ${empleado.nombre} ${empleado.apellido}", style = MaterialTheme.typography.titleLarge)
            Text("Usuario: ${empleado.usuario}", style = MaterialTheme.typography.bodyLarge)
            Text("Teléfono: ${empleado.telefono ?: "No especificado"}", style = MaterialTheme.typography.bodyLarge)
            Text("Fecha: ${empleado.fecha ?: "No especificada"}", style = MaterialTheme.typography.bodyLarge)
            Text("Calle: ${empleado.calle ?: "No especificada"}", style = MaterialTheme.typography.bodyLarge)
            Text("Número: ${empleado.numero ?: "No especificado"}", style = MaterialTheme.typography.bodyLarge)
            Text("Disponibilidad: ${if (empleado.disponibilidad == true) "Disponible" else "No disponible"}", style = MaterialTheme.typography.bodyLarge)
            Text("Colonia: $coloniaName", style = MaterialTheme.typography.bodyLarge)
            Text("Especialidad: $especialidadName", style = MaterialTheme.typography.bodyLarge)
            Text("Puesto: $puestoName", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoCreateScreen(viewModel: EmpleadoViewModel = viewModel(), onSaved: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val colonias by viewModel.colonias.collectAsState()
    val especialidades by viewModel.especialidades.collectAsState()
    val puestos by viewModel.puestos.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var disponibilidad by remember { mutableStateOf(true) }

    var selectedColonia by remember { mutableStateOf<Colonia?>(null) }
    var selectedEspecialidad by remember { mutableStateOf<Especialidad?>(null) }
    var selectedPuesto by remember { mutableStateOf<Puesto?>(null) }

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
                title = { Text("Nuevo Empleado") },
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
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
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
                    label = { Text("Fecha (AAAA-MM-DD)") },
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Disponible")
                Switch(
                    checked = disponibilidad,
                    onCheckedChange = { disponibilidad = it }
                )
            }
            ColoniaDropdown(
                label = "Colonia (opcional)",
                items = colonias,
                selectedItem = selectedColonia,
                onItemSelected = { selectedColonia = it }
            )
            EspecialidadDropdown(
                label = "Especialidad (opcional)",
                items = especialidades,
                selectedItem = selectedEspecialidad,
                onItemSelected = { selectedEspecialidad = it }
            )
            PuestoDropdown(
                label = "Puesto (opcional)",
                items = puestos,
                selectedItem = selectedPuesto,
                onItemSelected = { selectedPuesto = it }
            )

            Button(onClick = { cameraLauncher.launch(null) }, modifier = Modifier.fillMaxWidth()) {
                Text("Tomar Fotografía")
            }

            if (capturedBitmap != null) {
                Image(
                    bitmap = capturedBitmap!!.asImageBitmap(),
                    contentDescription = "Foto del empleado",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }

            Button(
                onClick = {
                    val colId = selectedColonia?.id_colonia
                    val munId = selectedColonia?.id_municipio
                    val espId = selectedEspecialidad?.id_especialidad
                    val puesId = selectedPuesto?.id_puesto
                    if (nombre.isNotBlank() && apellido.isNotBlank() && usuario.isNotBlank() && contrasena.isNotBlank()) {
                        viewModel.createEmpleado(
                            nombre, apellido, usuario, contrasena, telefono, fecha, calle, numero,
                            disponibilidad, munId, colId, espId, puesId, fotoBase64
                        ) {
                            onSaved()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Guardar Empleado")
            }
        }
    }
}
