package com.example.mardeluna

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mardeluna.ui.theme.MarDeLunaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarDeLunaTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("main_logo") { MainLogoScreen(navController) }
        composable("second_floor") { SecondFloorScreen(navController) }
        composable("icu_screen") { ICUScreen(navController) }
        composable("surgery_screen") { SurgeryScreen(navController) }
        composable("hospitalization_screen") { HospitalizationScreen(navController) }
        composable("room_screen") { RoomScreen(navController) }
        composable("patient_aspiration_screen") { PatientAspirationScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
    }
}

@Composable
fun StartScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("history") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "¿Quiénes somos?")
        }
        Spacer(modifier = Modifier.height(32.dp))
        LoginSection(navController)
        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable
fun LoginSection(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
    var email by remember { mutableStateOf(sharedPreferences.getString("last_email", "") ?: "") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(
            onClick = {
                errorMessage = ""
                if (email.matches(emailRegex) && password.length > 11) {
                    // Guardar el último correo y contraseña en SharedPreferences
                    with(sharedPreferences.edit()) {
                        putString("last_email", email)
                        apply()
                    }
                    navController.navigate("main_logo")
                } else {
                    errorMessage = "Error de inicio de sesión"
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Iniciar sesión")
        }
    }
}

@Composable
fun MainLogoScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.piso_2_logo),
            contentDescription = "Piso 2 Logo",
            modifier = Modifier
                .size(400.dp)
                .clickable { navController.navigate("second_floor") }
        )
    }

}

@Composable
fun SecondFloorScreen(navController: NavHostController) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 3f)
                        offsetX = (offsetX + pan.x * scale).coerceIn(-500f, 500f)
                        offsetY = (offsetY + pan.y * scale).coerceIn(-500f, 500f)
                    }
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.segunda_planta),
                contentDescription = "Segunda Planta",
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    )
                    .fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IndexItem(navController, "Unidad de cuidados intensivos", "icu_screen")
            IndexItem(navController, "Quirófanos", "surgery_screen")
            IndexItem(navController, "Hospitalización", "hospitalization_screen")
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    var showPassword by remember { mutableStateOf(false) }
    val workerId = "123456"
    val name = "Juan"
    val surname = "Pérez"
    val email = "juan.perez@hospital.com"
    val password = "123456789012"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perfil del Trabajador",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(text = "ID: $workerId")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Nombre: $name")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Apellidos: $surname")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Email: $email")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Contraseña:")
        Row {
            Text(
                text = if (showPassword) password else "*".repeat(password.length),
                modifier = Modifier.padding(end = 8.dp)
            )
            TextButton(onClick = { showPassword = !showPassword }) {
                Text(if (showPassword) "Ocultar" else "Mostrar")
            }
        }
    }
}

@Composable
fun HistoryScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Historia del Hospital Mar de Luna",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(text = """
            La Dirección y todo el personal que integra el Hospital Mar de Luna le damos una cálida bienvenida a su nuevo puesto.
            Para Hospital Mar de Luna, usted es de suma importancia y, por esta razón, queremos facilitarle sus primeros días con nosotros. Hemos preparado una guía didáctica que le explicará el funcionamiento del mismo.
            Nuestro propósito es hacer que sus primeros días sean más sencillos, facilitar su proceso de adaptación y hacer más cómoda su rutina diaria.
            Deseamos que su permanencia en nuestro centro le permita crecer profesionalmente y nos ayude a mejorar la calidad de la atención que brindamos a nuestros pacientes.
            Nuestro lema es siempre poner al paciente en el centro del cuidado y sabemos que esto no es posible sin cuidar al profesional. Por ello, consideramos esta guía como el inicio de una relación que esperamos sea beneficiosa para ambas partes.

            Historia, presente pasado y futuro
            En 2004 comenzamos nuestras actividades y, desde entonces, nuestra dedicación al cuidado de la salud de los pacientes y sus familias nos ha permitido convertirnos en el hospital de referencia en la zona de Mestalla, Valencia. Nuestro centro opera de manera que garantiza que nuestros pacientes reciban la más alta calidad en atención y procesos asistenciales.

            Pilares básicos
            · Razón de ser, el paciente.
            · Colaboración con el profesional.
            · Promoción interna y retención del talento.
            · Sistema de gestión basado en la calidad.
            · Permanente actualización tecnológica.
            · Búsqueda de una oferta médica integral.
            · Apuesta por la complejidad o Equilibrio Empresa y Hospital.

            Instalaciones Mar de Luna
            · 20 Consultas.
            · 70 camas.
            · 6 Quirófanos.
            · Diagnóstico por imagen.

            Compromiso con el Medio Ambiente
            Disponemos de un Programa de Mejora Ambiental, cuyos objetivos se van modificando según se consiguen. Algunos ejemplos son:
            · Reducción del consumo de agua en los centros.
            · Mejora de la eficiencia hídrica en el riego de jardines.
            · Inversión en iluminación LED.
            · Implantación del Sistema de Gestión Energética ISO 50001.
            · Reducción de la generación de residuos biosanitarios especiales.
        """.trimIndent())

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun IndexItem(navController: NavHostController, name: String, route: String) {
    Button(onClick = { navController.navigate(route) }, modifier = Modifier.fillMaxWidth()) {
        Text(text = name)
    }
}

@Composable
fun ICUScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pendiente de actualizar")
    }
}

@Composable
fun SurgeryScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pendiente de actualizar")
    }
}

@Composable
fun HospitalizationScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pendiente de actualizar")
    }
}

@Composable
fun RoomScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pendiente de actualizar")
    }
}

@Composable
fun PatientAspirationScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pendiente de actualizar")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MarDeLunaTheme {
        val navController = rememberNavController()
        AppNavigation(navController)
    }
}