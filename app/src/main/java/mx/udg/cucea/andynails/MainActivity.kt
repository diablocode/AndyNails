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
import mx.udg.cucea.andynails.ui.screens.CitasScreen
import mx.udg.cucea.andynails.ui.screens.ClientesScreen
import mx.udg.cucea.andynails.ui.screens.EmpleadosScreen
import mx.udg.cucea.andynails.ui.screens.PuestosScreen
import mx.udg.cucea.andynails.ui.screens.ServiciosScreen
import mx.udg.cucea.andynails.ui.theme.AndyNailsTheme

sealed interface CurrentScreen {
    object Dashboard : CurrentScreen
    object Servicios : CurrentScreen
    object Puestos : CurrentScreen
    object Clientes : CurrentScreen
    object Empleados : CurrentScreen
    object Citas : CurrentScreen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndyNailsTheme {
                var currentScreen by remember { mutableStateOf<CurrentScreen>(CurrentScreen.Dashboard) }

                when (currentScreen) {
                    is CurrentScreen.Dashboard -> {
                        DashboardScreen(
                            onNavigateServicios = { currentScreen = CurrentScreen.Servicios },
                            onNavigatePuestos = { currentScreen = CurrentScreen.Puestos },
                            onNavigateClientes = { currentScreen = CurrentScreen.Clientes },
                            onNavigateEmpleados = { currentScreen = CurrentScreen.Empleados },
                            onNavigateCitas = { currentScreen = CurrentScreen.Citas }
                        )
                    }
                    is CurrentScreen.Servicios -> {
                        ServiciosScreen(onBack = { currentScreen = CurrentScreen.Dashboard })
                    }
                    is CurrentScreen.Puestos -> {
                        PuestosScreen(onBack = { currentScreen = CurrentScreen.Dashboard })
                    }
                    is CurrentScreen.Clientes -> {
                        ClientesScreen(onBack = { currentScreen = CurrentScreen.Dashboard })
                    }
                    is CurrentScreen.Empleados -> {
                        EmpleadosScreen(onBack = { currentScreen = CurrentScreen.Dashboard })
                    }
                    is CurrentScreen.Citas -> {
                        CitasScreen(onBack = { currentScreen = CurrentScreen.Dashboard })
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
            TopAppBar(title = { Text("Andy Nails - Admin Dashboard") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Bienvenido a Andy Nails", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNavigateCitas,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Gestionar Citas (Agenda)")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateClientes,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Gestionar Clientes")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateEmpleados,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Gestionar Empleados")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateServicios,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Gestionar Servicios")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigatePuestos,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Gestionar Puestos")
            }
        }
    }
}
