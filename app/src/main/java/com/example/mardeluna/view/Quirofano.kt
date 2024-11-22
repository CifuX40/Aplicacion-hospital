package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.compose.ui.layout.*
import coil.compose.*
import com.google.firebase.ktx.*
import com.google.firebase.storage.ktx.*

@Composable
fun SurgeryScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }

    // Cargar fondo desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = Firebase.storage

        val backgroundRef = storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception -> Log.e("Firebase", "Error al cargar fondo: ${exception.message}") }
    }

    // Contenedor con fondo
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla
        if (backgroundUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
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
            // Título en color negro
            Text(
                text = "Quirófano",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,  // Letra en negro
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Cargar imágenes desde Firebase Storage
            FirebaseImage("gs://mar-de-luna-ada79.firebasestorage.app/rea.jpg", "Área de recuperación")
            FirebaseImage("gs://mar-de-luna-ada79.firebasestorage.app/sala_quirofano.jpg", "Sala de quirófano")
            FirebaseImage("gs://mar-de-luna-ada79.firebasestorage.app/esterilizacion.jpg", "Área de esterilización")
        }
    }
}

@Composable
fun FirebaseImage(storageUrl: String, description: String) {
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL de Firebase Storage
    LaunchedEffect(storageUrl) {
        val storage = Firebase.storage
        val storageRef = storage.getReferenceFromUrl(storageUrl)
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        when {
            imageUrl != null -> {
                // Mostrar la imagen si se obtuvo la URL correctamente
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = description,
                    modifier = Modifier.size(200.dp)
                )
            }
            loadError -> {
                Text("Error al cargar la imagen: $description", color = Color.Black)
            }
            else -> {
                Text("Cargando imagen...", color = Color.Black)
            }
        }

        Text(text = description, modifier = Modifier.padding(8.dp), color = Color.Black)
    }
}