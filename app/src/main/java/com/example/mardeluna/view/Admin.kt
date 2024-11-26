package com.example.mardeluna.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import com.google.firebase.firestore.*

// Pantalla de administración de usuarios
@Composable
fun AdminScreen(navController: NavHostController) {
    val firestore = remember { Firestore() }
    var dni by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Administración de Usuarios", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // DNI Input
        OutlinedTextField(
            value = dni,
            onValueChange = { dni = it },
            label = { Text("DNI") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Last Name Input
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Buttons for Create, Update, Delete
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
                            Log.e(
                                "Firestore",
                                "Error al crear usuario",
                                exception
                            )
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
    }
}

// Data class for User
data class User(
    val dni: String = "",
    val name: String = "",
    val lastName: String = "",
    val email: String = ""
)

// Firestore operations for adding, updating, and deleting users
class Firestore {
    private val db = FirebaseFirestore.getInstance()

    // Adds or overwrites a user in the "Usuarios" collection
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

    // Updates an existing user's fields
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

    // Deletes a user by their DNI
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