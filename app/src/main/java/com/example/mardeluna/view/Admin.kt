package com.example.mardeluna.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    var dni by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var userList by remember { mutableStateOf<List<User>>(emptyList()) }

    // Cargar el fondo desde Firebase Storage
    LaunchedEffect(Unit) {
        val backgroundRef =
            Firebase.storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error al cargar fondo: ${exception.message}")
            }

        // Cargar la lista de usuarios
        firestore.getAllUsers(
            onSuccess = { users -> userList = users },
            onError = { exception ->
                Log.e("Firestore", "Error al obtener usuarios: ${exception.message}")
            }
        )
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
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if (dni.isBlank() || email.isBlank() || name.isBlank() || lastName.isBlank()) {
                        message = "Todos los campos son obligatorios."
                    } else {
                        auth.createUserWithEmailAndPassword(email, dni)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    firestore.addUserToFirestore(
                                        userId = dni,
                                        name = name,
                                        lastName = lastName,
                                        email = email,
                                        onSuccess = {
                                            message = "Usuario creado con éxito."
                                            firestore.getAllUsers(
                                                onSuccess = { users -> userList = users },
                                                onError = { exception ->
                                                    Log.e(
                                                        "Firestore",
                                                        "Error al actualizar lista: ${exception.message}"
                                                    )
                                                }
                                            )
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

                Button(onClick = {
                    if (dni.isBlank()) {
                        message = "DNI es obligatorio para eliminar."
                    } else {
                        firestore.deleteUser(dni,
                            onSuccess = {
                                message = "Usuario eliminado con éxito."
                                firestore.getAllUsers(
                                    onSuccess = { users -> userList = users },
                                    onError = { exception ->
                                        Log.e(
                                            "Firestore",
                                            "Error al actualizar lista: ${exception.message}"
                                        )
                                    }
                                )
                            },
                            onError = { exception ->
                                message = "Error al eliminar usuario: ${exception.message}"
                            }
                        )
                    }
                }) {
                    Text("Eliminar Usuario")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(userList) { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("DNI: ${user.dni}", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "Nombre: ${user.name} ${user.lastName}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text("Email: ${user.email}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

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

data class User(
    val dni: String = "",
    val name: String = "",
    val lastName: String = "",
    val email: String = ""
)

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
            "dni" to userId,
            "name" to name,
            "lastName" to lastName,
            "email" to email
        )

        db.collection("Usuarios")
            .document(userId)
            .set(userMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun deleteUser(dni: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("Usuarios").document(dni).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun getAllUsers(onSuccess: (List<User>) -> Unit, onError: (Exception) -> Unit) {
        db.collection("Usuarios").get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.documents.mapNotNull { it.toObject(User::class.java) }
                onSuccess(users)
            }
            .addOnFailureListener { onError(it) }
    }
}