package mx.udg.cucea.andynails.ui.screens

import android.graphics.Bitmap
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.udg.cucea.andynails.ui.viewmodel.EmpleadoUiState
import mx.udg.cucea.andynails.ui.viewmodel.EmpleadoViewModel
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadosScreen(viewModel: EmpleadoViewModel = viewModel(), onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var disponibilidad by remember { mutableStateOf(true) }
    var idMunicipio by remember { mutableStateOf("") }
    var idColonia by remember { mutableStateOf("") }
    var idEspecialidad by remember { mutableStateOf("") }
    var idPuesto by remember { mutableStateOf("") }

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
            FloatingActionButton(onClick = { showDialog = true }) {
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
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Nuevo Empleado") },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            OutlinedTextField(
                                value = nombre,
                                onValueChange = { nombre = it },
                                label = { Text("Nombre *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = apellido,
                                onValueChange = { apellido = it },
                                label = { Text("Apellido *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = usuario,
                                onValueChange = { usuario = it },
                                label = { Text("Usuario *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = contrasena,
                                onValueChange = { contrasena = it },
                                label = { Text("Contraseña *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = telefono,
                                onValueChange = { telefono = it },
                                label = { Text("Teléfono (opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = fecha,
                                onValueChange = { fecha = it },
                                label = { Text("Fecha (AAAA-MM-DD) (opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = calle,
                                onValueChange = { calle = it },
                                label = { Text("Calle (opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = numero,
                                onValueChange = { numero = it },
                                label = { Text("Número (opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
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
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = idMunicipio,
                                onValueChange = { idMunicipio = it },
                                label = { Text("ID Municipio (opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = idColonia,
                                onValueChange = { idColonia = it },
                                label = { Text("ID Colonia (opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = idEspecialidad,
                                onValueChange = { idEspecialidad = it },
                                label = { Text("ID Especialidad (opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = idPuesto,
                                onValueChange = { idPuesto = it },
                                label = { Text("ID Puesto (opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Button(onClick = { cameraLauncher.launch(null) }, modifier = Modifier.fillMaxWidth()) {
                                Text("Tomar Fotografía")
                            }

                            if (capturedBitmap != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Image(
                                    bitmap = capturedBitmap!!.asImageBitmap(),
                                    contentDescription = "Foto del empleado",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                )
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            val munId = idMunicipio.toIntOrNull()
                            val colId = idColonia.toIntOrNull()
                            val espId = idEspecialidad.toIntOrNull()
                            val puesId = idPuesto.toIntOrNull()
                            if (nombre.isNotBlank() && apellido.isNotBlank() && usuario.isNotBlank() && contrasena.isNotBlank()) {
                                viewModel.createEmpleado(
                                    nombre, apellido, usuario, contrasena, telefono, fecha, calle, numero,
                                    disponibilidad, munId, colId, espId, puesId, fotoBase64
                                ) {
                                    showDialog = false
                                    nombre = ""
                                    apellido = ""
                                    usuario = ""
                                    contrasena = ""
                                    telefono = ""
                                    fecha = ""
                                    calle = ""
                                    numero = ""
                                    disponibilidad = true
                                    idMunicipio = ""
                                    idColonia = ""
                                    idEspecialidad = ""
                                    idPuesto = ""
                                    capturedBitmap = null
                                    fotoBase64 = null
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
