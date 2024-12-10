package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.auth.*
import com.google.firebase.firestore.*
import com.google.firebase.storage.*

@Composable
fun Publicaciones(navController: NavHostController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid
    var backgroundUrl by remember { mutableStateOf("") }
    var publicaciones by remember { mutableStateOf<List<Map<String, Any>>?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        cargarFondo { url -> backgroundUrl = url }
        cargarPublicaciones { lista ->
            publicaciones = lista
            loading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (backgroundUrl.isNotEmpty()) {
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
                    .background(Color.LightGray)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Publicaciones",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (loading) {
                CircularProgressIndicator()
            } else if (publicaciones.isNullOrEmpty()) {
                Text("No hay publicaciones disponibles.")
            } else {
                publicaciones?.forEach { publicacion ->
                    Publicacion(publicacion, userId) { postId ->
                        eliminarPublicacion(postId) {
                            publicaciones = publicaciones?.filter { it["id"] != postId }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("agregar_publicacion") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Añadir publicación")
            }
        }
    }
}

@Composable
fun Publicacion(
    publicacion: Map<String, Any>,
    currentUserId: String?,
    onDelete: (String) -> Unit
) {
    val postId = publicacion["id"] as? String ?: ""
    val userId = publicacion["userId"] as? String
    val mensaje = publicacion["mensaje"] as? String ?: "Sin contenido"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .border(1.dp, Color.Gray)
            .padding(8.dp)
    ) {
        Text(
            text = mensaje,
            fontSize = 16.sp,
            color = Color.Black
        )

        if (currentUserId == userId) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onDelete(postId) },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Eliminar")
            }
        }
    }
}

@Composable
fun AgregarPublicacionUI(navController: NavHostController) {
    var texto by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        cargarFondo { url -> backgroundUrl = url }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (backgroundUrl.isNotEmpty()) {
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
                    .background(Color.LightGray)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Añadir publicación",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Escribe tu mensaje aquí") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (texto.isNotBlank()) {
                    publicarPublicacion(texto) {
                        successMessage = "Publicación realizada con éxito!"
                        navController.navigate("publicaciones") {
                            popUpTo("publicaciones") { inclusive = true }
                        }
                    }
                } else {
                    errorMessage = "Debe ingresar un texto."
                }
            }) {
                Text("Publicar")
            }

            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (successMessage.isNotBlank()) {
                Text(
                    text = successMessage,
                    color = Color.Green,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

private fun cargarFondo(onSuccess: (String) -> Unit) {
    val storage = FirebaseStorage.getInstance()
    storage.reference.child("fondo_de_pantalla.jpg").downloadUrl
        .addOnSuccessListener { uri -> onSuccess(uri.toString()) }
        .addOnFailureListener { Log.e("Firebase", "Error al cargar fondo: ${it.message}") }
}

private fun cargarPublicaciones(onSuccess: (List<Map<String, Any>>?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("publicaciones")
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .get()
        .addOnSuccessListener { snapshot ->
            val publicaciones = snapshot.documents.mapNotNull { document ->
                document.data?.toMutableMap()?.apply {
                    put("id", document.id)
                }
            }
            Log.d("Firestore", "Publicaciones recuperadas: $publicaciones")
            onSuccess(publicaciones)
        }
        .addOnFailureListener {
            Log.e("Firestore", "Error al recuperar publicaciones: ${it.message}")
            onSuccess(emptyList())
        }
}

private fun eliminarPublicacion(postId: String, onComplete: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("publicaciones").document(postId)
        .delete()
        .addOnSuccessListener { onComplete() }
}

private fun publicarPublicacion(
    mensaje: String,
    onSuccess: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    val publicacion = hashMapOf(
        "mensaje" to mensaje,
        "userId" to currentUser?.uid,
        "timestamp" to FieldValue.serverTimestamp()
    )

    db.collection("publicaciones").add(publicacion).addOnSuccessListener {
        onSuccess()
    }
}