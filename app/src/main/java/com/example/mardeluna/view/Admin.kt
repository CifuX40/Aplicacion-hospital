package com.example.mardeluna.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@Composable
fun AdminScreen(navController: NavHostController) {
    val firestore = remember { Firestore() }
    val auth = remember { FirebaseAuth.getInstance() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }

    // Cargar el fondo desde Firebase Storage
    val backgroundRef =
        Firebase.storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
    backgroundRef.downloadUrl
        .addOnSuccessListener { uri ->
            backgroundUrl = uri.toString()
        }
        .addOnFailureListener { exception ->
            Log.e("Firebase", "Error al cargar fondo: ${exception.message}")
        }

    Box(modifier = Modifier.fillMaxSize()) {
        if (backgroundUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Administración de usuarios", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (email.isBlank() || password.isBlank() || name.isBlank() || lastName.isBlank()) {
                    message = "Todos los campos son obligatorios."
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = task.result?.user?.uid ?: return@addOnCompleteListener
                                firestore.addUserToFirestore(
                                    userId = userId,
                                    name = name,
                                    lastName = lastName,
                                    email = email,
                                    onSuccess = {
                                        message = "Usuario creado con éxito."
                                    },
                                    onError = { exception ->
                                        message =
                                            "Error al guardar en Firestore: ${exception.message}"
                                    }
                                )
                            } else {
                                message = "Error al crear usuario: ${task.exception?.message}"
                            }
                        }
                }
            }) {
                Text("Crear Usuario")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("start") {
                        popUpTo("start") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

class Firestore {
    private val db = FirebaseFirestore.getInstance()

    fun addUserToFirestore(
        userId: String,
        name: String,
        lastName: String,
        email: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val userMap = mapOf(
            "name" to name,
            "lastName" to lastName,
            "email" to email
        )

        db.collection("Usuarios")
            .document(userId)
            .set(userMap)
            .addOnSuccessListener {
                Log.d("Firestore", "Usuario guardado en Firestore con ID: $userId")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error al guardar usuario en Firestore", exception)
                onError(exception)
            }
    }
}