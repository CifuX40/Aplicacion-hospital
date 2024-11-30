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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// Pantalla de administración de usuarios
@Composable
fun AdminScreen(navController: NavHostController) {
    val firestore = remember { Firestore() }
    var dni by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }

    // Obtener referencia del fondo de pantalla desde Firebase Storage
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
                value = dni,
                onValueChange = { dni = it },
                label = { Text("DNI") },
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
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if (dni.isBlank() || name.isBlank() || lastName.isBlank() || email.isBlank()) {
                        message = "Todos los campos son obligatorios."
                    } else {
                        val user = User(dni, name, lastName, email)
                        firestore.addUser(user,
                            onSuccess = { },
                            onError = { exception ->
                                Log.e("Firestore", "Error al crear usuario", exception)
                            },
                            updateMessage = { newMessage -> message = newMessage }
                        )
                    }
                }) {
                    Text("Crear")
                }

                Button(onClick = {
                    firestore.updateUser(dni,
                        updatedFields = mapOf(
                            "name" to name,
                            "lastName" to lastName,
                            "email" to email
                        ),
                        onSuccess = { },
                        onError = { exception ->
                            message = "Error al actualizar usuario: ${exception.message}"
                        },
                        updateMessage = { newMessage -> message = newMessage }
                    )
                }) {
                    Text("Actualizar")
                }

                Button(onClick = {
                    firestore.deleteUser(dni,
                        onSuccess = { },
                        onError = { exception ->
                            message = "Error al eliminar usuario: ${exception.message}"
                        },
                        updateMessage = { newMessage -> message = newMessage }
                    )
                }) {
                    Text("Eliminar")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Display Message
            if (message.isNotEmpty()) {
                Text(
                    message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de cerrar sesión
            Button(
                onClick = {
                    // Navegar de regreso a StartScreen
                    navController.navigate("StartScreen") {
                        popUpTo("AdminScreen") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

data class User(
    val dni: String = "",
    val name: String = "",
    val lastName: String = "",
    val email: String = ""
)

class Firestore {
    private val db = FirebaseFirestore.getInstance()

    fun addUser(
        user: User,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
        updateMessage: (String) -> Unit
    ) {
        db.collection("Usuarios")
            .document(user.dni)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.exists()) {
                    db.collection("Usuarios")
                        .document(user.dni)
                        .set(user)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Usuario agregado correctamente: ${user.dni}")
                            updateMessage("Usuario creado con éxito.")
                            onSuccess()
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firestore", "Error al agregar usuario", exception)
                            updateMessage("Error al crear usuario: ${exception.message}")
                            onError(exception)
                        }
                } else {
                    Log.d("Firestore", "El usuario ya existe: ${user.dni}")
                    updateMessage("El usuario ya existe.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error al verificar si el usuario existe", exception)
                updateMessage("Error al verificar si el usuario existe: ${exception.message}")
                onError(exception)
            }
    }

    fun updateUser(
        dni: String,
        updatedFields: Map<String, Any>,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
        updateMessage: (String) -> Unit
    ) {
        db.collection("Usuarios")
            .document(dni)
            .update(updatedFields)
            .addOnSuccessListener {
                Log.d("Firestore", "Usuario actualizado correctamente: $dni")
                updateMessage("Usuario actualizado con éxito.")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error al actualizar usuario", exception)
                updateMessage("Error al actualizar usuario: ${exception.message}")
                onError(exception)
            }
    }

    fun deleteUser(
        dni: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
        updateMessage: (String) -> Unit
    ) {
        db.collection("Usuarios")
            .document(dni)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Usuario eliminado correctamente: $dni")
                updateMessage("Usuario eliminado con éxito.")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error al eliminar usuario", exception)
                updateMessage("Error al eliminar usuario: ${exception.message}")
                onError(exception)
            }
    }
}