package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import com.google.firebase.auth.*
import com.google.firebase.firestore.*
import com.google.firebase.ktx.*
import coil.compose.*
import com.google.firebase.storage.ktx.*

data class User(val dni: String, val name: String, val lastName: String, val email: String)

@Composable
fun Admin(navController: NavHostController) {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    var dni by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var userList by remember { mutableStateOf<List<User>>(emptyList()) }

    LaunchedEffect(Unit) {
        loadBackgroundImage { uri -> backgroundUrl = uri }
        getAllUsers(firestore,
            onSuccess = { userList = it },
            onError = { exception ->
                Log.e(
                    "Firestore",
                    "Error al obtener usuarios: ${exception.message}"
                )
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

        AdminContent(
            dni = dni,
            email = email,
            name = name,
            lastName = lastName,
            userList = userList,
            message = message,
            onDniChange = { dni = it },
            onEmailChange = { email = it },
            onNameChange = { name = it },
            onLastNameChange = { lastName = it },
            onCreateUser = {
                if (validateFields(dni, email, name, lastName)) {
                    createUser(auth, firestore, dni, email, name, lastName) { msg ->
                        message = msg
                        getAllUsers(firestore,
                            onSuccess = { userList = it },
                            onError = { exception ->
                                Log.e(
                                    "Firestore",
                                    "Error al actualizar usuarios: ${exception.message}"
                                )
                            }
                        )
                    }
                } else {
                    message = "Todos los campos son obligatorios."
                }
            },
            onDeleteUser = {
                if (validateFields(dni, email)) {
                    deleteUser(auth, firestore, dni, email) { msg ->
                        message = msg
                        getAllUsers(firestore,
                            onSuccess = { userList = it },
                            onError = { exception ->
                                Log.e(
                                    "Firestore",
                                    "Error al actualizar usuarios: ${exception.message}"
                                )
                            }
                        )
                    }
                } else {
                    message = "DNI y Email son obligatorios para eliminar."
                }
            },
            onLogout = {
                navController.navigate("iniciar_sesion") {
                    popUpTo("iniciar_sesion") {
                        inclusive = true
                    }
                }
            }
        )
    }
}

@Composable
fun AdminContent(
    dni: String,
    email: String,
    name: String,
    lastName: String,
    userList: List<User>,
    message: String,
    onDniChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onCreateUser: () -> Unit,
    onDeleteUser: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Administración de usuarios", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = dni,
            onValueChange = onDniChange,
            label = { Text("DNI") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = onCreateUser) { Text("Crear Usuario") }
            Button(onClick = onDeleteUser) { Text("Eliminar Usuario") }
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

        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) { Text("Cerrar sesión") }
    }
}

fun validateFields(vararg fields: String): Boolean = fields.all { it.isNotBlank() }
fun loadBackgroundImage(onSuccess: (String) -> Unit) {
    Firebase.storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
        .downloadUrl
        .addOnSuccessListener { uri -> onSuccess(uri.toString()) }
        .addOnFailureListener { Log.e("Firebase", "Error al cargar fondo: ${it.message}") }
}

fun getAllUsers(
    firestore: FirebaseFirestore,
    onSuccess: (List<User>) -> Unit,
    onError: (Exception) -> Unit
) {
    firestore.collection("Usuarios")
        .get()
        .addOnSuccessListener { result ->
            val users = result.documents.map { document ->
                User(
                    dni = document.getString("dni") ?: "",
                    name = document.getString("name") ?: "",
                    lastName = document.getString("lastName") ?: "",
                    email = document.getString("email") ?: ""
                )
            }
            onSuccess(users)
        }
        .addOnFailureListener { exception ->
            onError(exception)
        }
}

fun createUser(
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    dni: String,
    email: String,
    name: String,
    lastName: String,
    onResult: (String) -> Unit
) {
    firestore.collection("Usuarios")
        .document(dni)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                onResult("Error: El DNI ya existe.")
            } else {
                auth.createUserWithEmailAndPassword(email, dni)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = mapOf(
                                "dni" to dni,
                                "name" to name,
                                "lastName" to lastName,
                                "email" to email
                            )

                            firestore.collection("Usuarios")
                                .document(dni)
                                .set(user)
                                .addOnSuccessListener {
                                    onResult("Usuario creado con éxito.")
                                }
                                .addOnFailureListener { e ->
                                    onResult("Error al guardar en Firestore: ${e.message}")
                                }
                        } else {
                            onResult("Error al crear usuario en Authentication: ${task.exception?.message}")
                        }
                    }
            }
        }
        .addOnFailureListener { e ->
            onResult("Error al verificar el DNI: ${e.message}")
        }
}

fun deleteUser(
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    dni: String,
    email: String,
    onResult: (String) -> Unit
) {
    firestore.collection("Usuarios")
        .document(dni)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists() && document.getString("email") == email) {
                document.reference.delete()
                    .addOnSuccessListener {
                        onResult("Usuario eliminado con éxito.")
                    }
                    .addOnFailureListener { e ->
                        onResult("Error al eliminar usuario de Firestore: ${e.message}")
                    }
            } else {
                onResult("Error: El DNI y el correo electrónico no coinciden.")
            }
        }
        .addOnFailureListener { e ->
            onResult("Error al buscar el usuario: ${e.message}")
        }
}