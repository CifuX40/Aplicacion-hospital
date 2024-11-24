package com.example.mardeluna.view

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.example.mardeluna.R
import com.google.firebase.auth.*
import com.google.firebase.storage.FirebaseStorage
import androidx.compose.ui.platform.LocalContext

@Composable
fun StartScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()

    // Recordar la última cuenta utilizada
    val sharedPrefs: SharedPreferences = LocalContext.current.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val savedEmail = sharedPrefs.getString("email", null)
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar imagen de fondo
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception -> loadError = true }

        if (savedEmail != null) {
            email = savedEmail // Si hay un correo guardado, lo ponemos en el campo de texto.
        }
    }

    fun loginUser() {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser: FirebaseUser? = auth.currentUser
                        val userEmail = currentUser?.email
                        // Guardar el email en SharedPreferences para recordarlo la próxima vez
                        sharedPrefs.edit().putString("email", email).apply()

                        if (userEmail == "admin@mardeluna.com") {
                            navController.navigate("admin") {
                                popUpTo("start") { inclusive = true }
                            }
                        } else {
                            navController.navigate("main_logo") {
                                popUpTo("start") { inclusive = true }
                            }
                        }
                    } else {
                        loginError = "Error al iniciar sesión: ${task.exception?.message}"
                    }
                }
        } else {
            loginError = "Por favor ingresa correo y contraseña"
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla
        if (backgroundUrl.isNotEmpty() && !loadError) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (loadError) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Gray))
        }

        // Contenido de la pantalla de inicio de sesión
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo de la app
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp).padding(bottom = 16.dp)
            )

            // Campos de correo y contraseña
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar error si existe
            if (loginError.isNotEmpty()) {
                Text(
                    text = loginError,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de inicio de sesión
            Button(
                onClick = { loginUser() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Iniciar sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de la historia del hospital
            Button(
                onClick = { navController.navigate("history") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Historia del Hospital")
            }
        }
    }
}