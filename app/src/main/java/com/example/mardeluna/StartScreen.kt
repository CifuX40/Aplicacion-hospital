package com.example.mardeluna

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.auth.*
import com.google.firebase.storage.*

@Composable
fun StartScreen(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la imagen desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("logo.png")

        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "Imagen cargada exitosamente: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen: ${exception.message}")
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Usar Coil para cargar la imagen desde la URL obtenida de Firebase Storage
        if (imageUrl.isNotEmpty() && !loadError) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
        } else {
            // En caso de error, muestra un mensaje
            Text(text = "Error al cargar el logo", color = MaterialTheme.colorScheme.error)
        }

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
    val credentialsManager =
        CredentialsManager(context)  // Crear instancia del gestor de credenciales

    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Obtener la lista de usuarios guardados
    val users = credentialsManager.getAllUsers()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Selecciona un usuario:", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar la lista de usuarios guardados
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
                                // Guardar el usuario y contraseña en SharedPreferences
                                credentialsManager.saveUser(email, contrasena)
                                navController.navigate("main_logo")
                            } else {
                                // Error en el inicio de sesión
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