package com.example.mardeluna.view

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

@Composable
fun PublicacionesScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var publicaciones by remember { mutableStateOf<List<Map<String, Any>>?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        cargarFondo { url ->
            backgroundUrl = url
        }
        cargarPublicaciones { lista ->
            publicaciones = lista
            loading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla
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

        // Contenido principal
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
                    PublicacionItem(publicacion)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para añadir publicación
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
fun PublicacionItem(publicacion: Map<String, Any>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
    ) {
        val mensaje = publicacion["mensaje"] as? String ?: "Sin mensaje"
        val imagen = publicacion["imagen"] as? String

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
    }
}

@Composable
fun AgregarPublicacionUI(navController: NavHostController) {
    var texto by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") } // Añadido

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    // Cargar el fondo
    LaunchedEffect(Unit) {
        cargarFondo { url ->
            backgroundUrl = url
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla
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
            onSuccess(snapshot.documents.map { it.data ?: emptyMap() })
        }
        .addOnFailureListener {
            onSuccess(emptyList())
        }
}

private fun publicarPublicacion(
    mensaje: String,
    imageUri: Uri?,
    onSuccess: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance().reference

    val publicacion = hashMapOf(
        "mensaje" to mensaje,
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