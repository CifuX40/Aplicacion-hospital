package com.example.mardeluna

import android.content.Context
import android.content.SharedPreferences
import android.util.Log // Importar para utilizar logging
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import modelo.ClaseConexion

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
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    // Estado para los campos de entrada
    var email by remember { mutableStateOf(sharedPreferences.getString("last_email", "") ?: "") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de entrada para el email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada para la contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de error en caso de fallo
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Botón para iniciar sesión
        Button(
            onClick = {
                errorMessage = ""
                if (email.matches(emailRegex) && password.length > 11) {
                    // Guardar el último correo en SharedPreferences
                    with(sharedPreferences.edit()) {
                        putString("last_email", email)
                        apply()
                    }

                    // Lógica para conectarse a la base de datos
                    CoroutineScope(Dispatchers.IO).launch {
                        val objConexion = ClaseConexion().cadenaConexion()
                        // Verifica la conexión
                        if (objConexion != null) {
                            // Imprimir estado de conexión en consola
                            Log.d("DB Connection", "Conectado")
                            // Navegar a la pantalla principal (o donde sea necesario)
                            navController.navigate("main_logo")
                        } else {
                            // Imprimir estado de conexión en consola
                            Log.d("DB Connection", "Error en la conexión")
                        }
                    }
                } else {
                    errorMessage = "Error de inicio de sesión"
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}