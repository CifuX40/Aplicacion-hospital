package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*

@Composable
fun CarinaScreen(navController: NavHostController) {
    // Variables de estado para la URL de la imagen y el fondo
    var imageUrl by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar imágenes desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo de pantalla
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundUrl = uri.toString()
                Log.d("Firebase", "Fondo cargado exitosamente: $backgroundUrl")
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error al cargar el fondo: ${exception.message}")
            }

        // Cargar imagen principal
        val imageRef = storage.reference.child("carina.jpg")
        imageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "Imagen de Carina cargada exitosamente: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen de Carina: ${exception.message}")
            }
    }

    // Contenedor con fondo
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
                    .background(Color.Gray)
            )
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título "Carina"
            Text(
                text = "Carina",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen principal
            when {
                imageUrl.isNotEmpty() && !loadError -> {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Imagen de Carina",
                        modifier = Modifier.size(300.dp)
                    )
                }

                loadError -> {
                    Text(
                        text = "Error al cargar la imagen",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                else -> {
                    Text(
                        text = "Cargando imagen...",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}