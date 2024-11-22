package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.example.mardeluna.controller.CredentialsManager
import com.google.firebase.auth.*
import com.google.firebase.storage.*

@Composable
fun StartScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var logoUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }
    var logoLoadError by remember { mutableStateOf(false) }

    // Cargar la imagen de fondo desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Fondo
        val storageRef = storage.reference.child("fondo_de_pantalla.jpg")
        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundUrl = uri.toString()
                Log.d("Firebase", "Fondo cargado exitosamente: $backgroundUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar el fondo: ${exception.message}")
            }

        // Logo
        val logoRef = storage.reference.child("logo.png")
        logoRef.downloadUrl
            .addOnSuccessListener { uri ->
                logoUrl = uri.toString()
                Log.d("Firebase", "Logo cargado exitosamente: $logoUrl")
            }
            .addOnFailureListener { exception ->
                logoLoadError = true
                Log.e("Firebase", "Error al cargar el logo: ${exception.message}")
            }
    }

    // Diseño principal de la pantalla
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        if (backgroundUrl.isNotEmpty() && !loadError) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
        }

        // Contenido de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Encabezado y logo
            Text(
                text = "Bienvenido al Hospital Mar de Luna",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Imagen del logo
            if (logoUrl.isNotEmpty() && !logoLoadError) {
                Image(
                    painter = rememberAsyncImagePainter(logoUrl),
                    contentDescription = "Logo de Mar de Luna",
                    modifier = Modifier
                        .height(200.dp)
                        .width(200.dp),
                    contentScale = ContentScale.Fit
                )
            } else if (logoLoadError) {
                Text(
                    text = "Error al cargar el logo",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de navegación
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
}

@Composable
fun LoginSection(navController: NavHostController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val credentialsManager = CredentialsManager(context)

    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val users = credentialsManager.getAllUsers()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Selecciona un usuario:", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        users.forEach { (savedEmail, savedPassword) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        email = savedEmail
                        contrasena = savedPassword
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = savedEmail)
                }
                IconButton(onClick = { credentialsManager.clearUser(savedEmail) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar usuario"
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Usuario (Email)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
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
                if (email.isNotEmpty() && contrasena.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, contrasena)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                credentialsManager.saveUser(email, contrasena)
                                navController.navigate("main_logo")
                            } else {
                                errorMessage =
                                    "Error: ${task.exception?.message ?: "Error desconocido"}"
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