package com.example.mardeluna

import android.util.Log
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
import com.google.firebase.storage.FirebaseStorage

@Composable
fun PaginasWebScreen(navController: NavHostController) {
    // Variables de estado para las URLs de las imágenes
    var backgroundImageUrl by remember { mutableStateOf<String?>(null) }
    var webPageImageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }
    var secondLoadError by remember { mutableStateOf(false) }

    // Cargar imágenes desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo de pantalla
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundImageUrl = uri.toString()
                Log.d("Firebase", "Fondo cargado exitosamente: $backgroundImageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar el fondo: ${exception.message}")
            }

        // Cargar imagen de "Páginas web"
        val webPageRef = storage.reference.child("paginas_web.jpg")
        webPageRef.downloadUrl
            .addOnSuccessListener { uri ->
                webPageImageUrl = uri.toString()
                Log.d("Firebase", "Imagen de Páginas web cargada exitosamente: $webPageImageUrl")
            }
            .addOnFailureListener { exception ->
                secondLoadError = true
                Log.e("Firebase", "Error al cargar la imagen de Páginas web: ${exception.message}")
            }
    }

    // Contenedor principal con fondo
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Imagen de fondo que ocupa toda la pantalla
        if (backgroundImageUrl != null && !loadError) {
            Image(
                painter = rememberAsyncImagePainter(backgroundImageUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop, // Asegura que la imagen cubra toda la pantalla
                modifier = Modifier.fillMaxSize()
            )
        } else if (loadError) {
            // Fondo alternativo en caso de error al cargar la imagen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
        }

        // Contenido superpuesto al fondo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título principal "Páginas web" en negrita y color negro
            Text(
                text = "Páginas web",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Imagen de "Páginas web" desde Firebase Storage
            when {
                webPageImageUrl != null && !secondLoadError -> {
                    Image(
                        painter = rememberAsyncImagePainter(webPageImageUrl),
                        contentDescription = "Imagen de páginas web",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp) // Tamaño ajustado para mayor visibilidad
                            .padding(bottom = 24.dp) // Más espacio debajo de la primera imagen
                    )
                }

                secondLoadError -> {
                    Text(
                        text = "Error al cargar la imagen de Páginas web",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                else -> {
                    Text(
                        text = "Cargando imagen de Páginas web...",
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }

            // Descripción o contenido adicional
            Text(
                text = "Aquí puedes agregar contenido adicional relacionado con las páginas web de tu aplicación o información relevante para el usuario.",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}