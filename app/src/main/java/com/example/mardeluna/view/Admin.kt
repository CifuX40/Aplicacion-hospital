package com.example.mardeluna.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import com.google.firebase.firestore.*

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

        // Buttons
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
                        onSuccess = { message = "Usuario creado con éxito." },
                        onError = { exception ->
                            message = "Error al crear usuario: ${exception.message}"
                            Log.e("Firestore", "Error al crear usuario", exception)
                        }
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
                    onSuccess = { message = "Usuario actualizado con éxito." },
                    onError = { message = "Error al actualizar usuario: ${it.message}" }
                )
            }) {
                Text("Actualizar")
            }
            Button(onClick = {
                firestore.deleteUser(dni,
                    onSuccess = { message = "Usuario eliminado con éxito." },
                    onError = { message = "Error al eliminar usuario: ${it.message}" }
                )
            }) {
                Text("Eliminar")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Message Display
        if (message.isNotEmpty()) {
            Text(
                message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
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

    // Agrega o sobreescribe un usuario en la colección "Usuarios"
    fun addUser(user: User, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("Usuarios") // Si no existe, la colección "Usuarios" se crea automáticamente.
            .document(user.dni)
            .set(user)
            .addOnSuccessListener {
                Log.d("Firestore", "Usuario agregado correctamente: ${user.dni}")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error al agregar usuario", exception)
                onError(exception)
            }
    }

    // Actualiza los campos de un usuario existente
    fun updateUser(
        dni: String,
        updatedFields: Map<String, Any>,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("Usuarios")
            .document(dni) // Aquí se usa dni directamente
            .update(updatedFields)
            .addOnSuccessListener {
                Log.d("Firestore", "Usuario actualizado correctamente: $dni") // dni
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error al actualizar usuario", exception)
                onError(exception)
            }
    }

    // Elimina un usuario por su DNI
    fun deleteUser(dni: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("Usuarios")
            .document(dni) // Aquí se usa dni directamente
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Usuario eliminado correctamente: $dni") // dni
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error al eliminar usuario", exception)
                onError(exception)
            }
    }

    // Comprueba si la colección "Usuarios" tiene documentos y ejecuta una acción
    fun ensureCollectionExists(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("Usuarios")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // La colección no tiene documentos, pero existe al agregar el primero
                    onSuccess()
                } else {
                    // La colección ya contiene documentos
                    onSuccess()
                }
            }
            .addOnFailureListener { onError(it) }
    }
}