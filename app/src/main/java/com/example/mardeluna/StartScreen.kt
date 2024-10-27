package com.example.mardeluna

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

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
    var contrasena by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Expresión regular para validar correos que terminan en @gmail.com
    val gmailRegex = "^[A-Za-z0-9+_.-]+@gmail\\.com$".toRegex()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de entrada para el email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Usuario (Email)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada para la contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
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
                // Validar que el correo termine en @gmail.com y que la contraseña tenga al menos 12 caracteres
                if (email.matches(gmailRegex) && contrasena.length >= 12) {
                    // Guardar el último correo en SharedPreferences
                    with(sharedPreferences.edit()) {
                        putString("last_email", email)
                        apply()
                    }

                    // Simulación de inicio de sesión exitoso
                    Log.d("Login", "Inicio de sesión simulado exitoso")
                    navController.navigate("main_logo") // Cambia a la pantalla principal simulada
                } else {
                    // Si no se cumplen las condiciones, mostrar mensaje de error
                    errorMessage = "Error con el correo o contraseña"
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
