package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*

@Composable
fun CarroParadasScreen(navController: NavHostController) {
    // Variables de estado para las URLs de las imágenes
    var imageUrl by remember { mutableStateOf("") }
    var secondImageUrl by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }
    var secondLoadError by remember { mutableStateOf(false) }

    // Cargar las imágenes desde Firebase Storage
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
        val imageRef = storage.reference.child("carro_paradas.jpg")
        imageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "Imagen cargada exitosamente: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen: ${exception.message}")
            }

        // Cargar segunda imagen
        val secondImageRef = storage.reference.child("contenido_carro_paradas.jpg")
        secondImageRef.downloadUrl
            .addOnSuccessListener { uri ->
                secondImageUrl = uri.toString()
                Log.d("Firebase", "Segunda imagen cargada exitosamente: $secondImageUrl")
            }
            .addOnFailureListener { exception ->
                secondLoadError = true
                Log.e("Firebase", "Error al cargar la segunda imagen: ${exception.message}")
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Carro de paradas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Imagen principal
            when {
                imageUrl.isNotEmpty() && !loadError -> {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Imagen del carro de paradas",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .padding(bottom = 10.dp)
                    )
                }

                loadError -> {
                    Text(
                        text = "Error al cargar la imagen principal",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                else -> {
                    Text(
                        text = "Cargando imagen principal...",
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }

            // Segunda imagen
            when {
                secondImageUrl.isNotEmpty() && !secondLoadError -> {
                    Image(
                        painter = rememberAsyncImagePainter(secondImageUrl),
                        contentDescription = "Contenido del carro de paradas",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(bottom = 10.dp)
                    )
                }

                secondLoadError -> {
                    Text(
                        text = "Error al cargar la segunda imagen",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                else -> {
                    Text(
                        text = "Cargando segunda imagen...",
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}