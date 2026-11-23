package mx.udg.cucea.andynails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.udg.cucea.andynails.data.model.*
import mx.udg.cucea.andynails.ui.screens.*
import mx.udg.cucea.andynails.ui.theme.AndyNailsTheme

sealed interface CurrentScreen {
    object Dashboard : CurrentScreen

    object ServiciosList : CurrentScreen
    object ServicioCreate : CurrentScreen
    data class ServicioDetail(val servicio: Servicio) : CurrentScreen

    object PuestosList : CurrentScreen
    object PuestoCreate : CurrentScreen
    data class PuestoDetail(val puesto: Puesto) : CurrentScreen

    object ClientesList : CurrentScreen
    object ClienteCreate : CurrentScreen
    data class ClienteDetail(val cliente: Cliente) : CurrentScreen

    object EmpleadosList : CurrentScreen
    object EmpleadoCreate : CurrentScreen
    data class EmpleadoDetail(val empleado: Empleado) : CurrentScreen

    object CitasList : CurrentScreen
    object CitaCreate : CurrentScreen
    data class CitaDetail(val cita: Cita) : CurrentScreen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndyNailsTheme {
                var currentScreen by remember { mutableStateOf<CurrentScreen>(CurrentScreen.Dashboard) }

                when (val screen = currentScreen) {
                    is CurrentScreen.Dashboard -> {
                        DashboardScreen(
                            onNavigateServicios = { currentScreen = CurrentScreen.ServiciosList },
                            onNavigatePuestos = { currentScreen = CurrentScreen.PuestosList },
                            onNavigateClientes = { currentScreen = CurrentScreen.ClientesList },
                            onNavigateEmpleados = { currentScreen = CurrentScreen.EmpleadosList },
                            onNavigateCitas = { currentScreen = CurrentScreen.CitasList }
                        )
                    }
                    is CurrentScreen.ServiciosList -> {
                        ServiciosScreen(
                            onServicioClick = { s -> currentScreen = CurrentScreen.ServicioDetail(s) },
                            onCreateClick = { currentScreen = CurrentScreen.ServicioCreate },
                            onBack = { currentScreen = CurrentScreen.Dashboard }
                        )
                    }
                    is CurrentScreen.ServicioCreate -> {
                        ServicioCreateScreen(
                            onSaved = { currentScreen = CurrentScreen.ServiciosList },
                            onBack = { currentScreen = CurrentScreen.ServiciosList }
                        )
                    }
                    is CurrentScreen.ServicioDetail -> {
                        ServicioDetailScreen(
                            servicio = screen.servicio,
                            onBack = { currentScreen = CurrentScreen.ServiciosList }
                        )
                    }
                    is CurrentScreen.PuestosList -> {
                        PuestosScreen(
                            onPuestoClick = { p -> currentScreen = CurrentScreen.PuestoDetail(p) },
                            onCreateClick = { currentScreen = CurrentScreen.PuestoCreate },
                            onBack = { currentScreen = CurrentScreen.Dashboard }
                        )
                    }
                    is CurrentScreen.PuestoCreate -> {
                        PuestoCreateScreen(
                            onSaved = { currentScreen = CurrentScreen.PuestosList },
                            onBack = { currentScreen = CurrentScreen.PuestosList }
                        )
                    }
                    is CurrentScreen.PuestoDetail -> {
                        PuestoDetailScreen(
                            puesto = screen.puesto,
                            onBack = { currentScreen = CurrentScreen.PuestosList }
                        )
                    }
                    is CurrentScreen.ClientesList -> {
                        ClientesScreen(
                            onClientClick = { c -> currentScreen = CurrentScreen.ClienteDetail(c) },
                            onCreateClick = { currentScreen = CurrentScreen.ClienteCreate },
                            onBack = { currentScreen = CurrentScreen.Dashboard }
                        )
                    }
                    is CurrentScreen.ClienteCreate -> {
                        ClienteCreateScreen(
                            onSaved = { currentScreen = CurrentScreen.ClientesList },
                            onBack = { currentScreen = CurrentScreen.ClientesList }
                        )
                    }
                    is CurrentScreen.ClienteDetail -> {
                        ClienteDetailScreen(
                            cliente = screen.cliente,
                            onBack = { currentScreen = CurrentScreen.ClientesList }
                        )
                    }
                    is CurrentScreen.EmpleadosList -> {
                        EmpleadosScreen(
                            onEmpleadoClick = { e -> currentScreen = CurrentScreen.EmpleadoDetail(e) },
                            onCreateClick = { currentScreen = CurrentScreen.EmpleadoCreate },
                            onBack = { currentScreen = CurrentScreen.Dashboard }
                        )
                    }
                    is CurrentScreen.EmpleadoCreate -> {
                        EmpleadoCreateScreen(
                            onSaved = { currentScreen = CurrentScreen.EmpleadosList },
                            onBack = { currentScreen = CurrentScreen.EmpleadosList }
                        )
                    }
                    is CurrentScreen.EmpleadoDetail -> {
                        EmpleadoDetailScreen(
                            empleado = screen.empleado,
                            onBack = { currentScreen = CurrentScreen.EmpleadosList }
                        )
                    }
                    is CurrentScreen.CitasList -> {
                        CitasScreen(
                            onCitaClick = { ci -> currentScreen = CurrentScreen.CitaDetail(ci) },
                            onCreateClick = { currentScreen = CurrentScreen.CitaCreate },
                            onBack = { currentScreen = CurrentScreen.Dashboard }
                        )
                    }
                    is CurrentScreen.CitaCreate -> {
                        CitaCreateScreen(
                            onSaved = { currentScreen = CurrentScreen.CitasList },
                            onBack = { currentScreen = CurrentScreen.CitasList }
                        )
                    }
                    is CurrentScreen.CitaDetail -> {
                        CitaDetailScreen(
                            cita = screen.cita,
                            onBack = { currentScreen = CurrentScreen.CitasList }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateServicios: () -> Unit,
    onNavigatePuestos: () -> Unit,
    onNavigateClientes: () -> Unit,
    onNavigateEmpleados: () -> Unit,
    onNavigateCitas: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Andy Nails - Administración") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Panel de Control",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Seleccione el módulo que desea administrar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            DashboardButton(title = "📅 Gestionar Citas (Agenda)", onClick = onNavigateCitas)
            DashboardButton(title = "👥 Gestionar Clientes", onClick = onNavigateClientes)
            DashboardButton(title = "👩‍💼 Gestionar Empleados", onClick = onNavigateEmpleados)
            DashboardButton(title = "💅 Gestionar Servicios", onClick = onNavigateServicios)
            DashboardButton(title = "🏷️ Gestionar Puestos", onClick = onNavigatePuestos)
        }
    }
}

@Composable
fun DashboardButton(title: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
    }
}
