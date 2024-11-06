package com.example.mardeluna

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
import com.google.firebase.auth.FirebaseAuth

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
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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
                if (email.isNotEmpty() && contrasena.isNotEmpty()) {
                    // Llama al método de Firebase para autenticar al usuario
                    auth.signInWithEmailAndPassword(email, contrasena)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Inicio de sesión exitoso
                                Log.d("Login", "Inicio de sesión exitoso con Firebase")
                                navController.navigate("main_logo") // Cambia a la pantalla principal
                            } else {
                                // Error en el inicio de sesión
                                errorMessage = "Error: ${task.exception?.message ?: "Error desconocido"}"
                            }
                        }
                } else {
                    errorMessage = "Por favor, completa todos los campos"
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
