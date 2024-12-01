package com.example.mardeluna.view

import android.net.*
import android.util.*
import androidx.activity.compose.*
import androidx.activity.result.contract.*
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
fun PublicacionesScreen(navController: NavHostController) {
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
                    PublicacionItem(publicacion, userId) { postId ->
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
fun PublicacionItem(
    publicacion: Map<String, Any>,
    currentUserId: String?,
    onDelete: (String) -> Unit
) {
    val mensaje = publicacion["mensaje"] as? String ?: "Sin mensaje"
    val imagen = publicacion["imagen"] as? String
    val autor = publicacion["autor"] as? String ?: "Anónimo"
    val postId = publicacion["id"] as? String ?: ""
    val userId = publicacion["userId"] as? String

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Text(text = autor, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = mensaje, fontSize = 16.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        imagen?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Imagen de la publicación",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

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
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
    }

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
                text = "Añadir Publicación",
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

            Button(onClick = { launcher.launch("image/*") }) {
                Text("Seleccionar Imagen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (texto.isNotBlank() || imageUri != null) {
                    publicarPublicacion(texto, imageUri) {
                        successMessage = "Publicación realizada con éxito!"
                        navController.navigate("publicaciones_screen") {
                            popUpTo("publicaciones_screen") { inclusive = true }
                        }
                    }
                } else {
                    errorMessage = "Debe ingresar un mensaje o seleccionar una imagen."
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
            onSuccess(snapshot.documents.map {
                it.data?.toMutableMap()?.apply { put("id", it.id) } ?: emptyMap()
            })
        }
        .addOnFailureListener {
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
    imageUri: Uri?,
    onSuccess: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance().reference
    val currentUser = FirebaseAuth.getInstance().currentUser

    val publicacion = hashMapOf(
        "mensaje" to mensaje,
        "userId" to currentUser?.uid,
        "timestamp" to FieldValue.serverTimestamp()
    )

    if (imageUri != null) {
        val ref = storage.child("publicaciones/${System.currentTimeMillis()}.jpg")
        ref.putFile(imageUri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { uri ->
                publicacion["imagen"] = uri.toString()
                db.collection("publicaciones").add(publicacion).addOnSuccessListener {
                    onSuccess()
                }
            }
        }
    } else {
        db.collection("publicaciones").add(publicacion).addOnSuccessListener {
            onSuccess()
        }
    }
}