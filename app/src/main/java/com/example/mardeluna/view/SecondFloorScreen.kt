package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.navigation.compose.*
import coil.compose.*
import com.google.firebase.ktx.*
import com.google.firebase.storage.ktx.*

@Composable
fun SecondFloorScreen(navController: NavHostController = rememberNavController()) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var rotationState by remember { mutableFloatStateOf(1f) }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var backgroundUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la imagen de fondo y la imagen principal desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = Firebase.storage

        // Cargar el fondo de pantalla
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundUrl = uri.toString()
                Log.d("Firebase", "Fondo cargado exitosamente: $backgroundUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar el fondo: ${exception.message}")
            }

        // Cargar la imagen principal
        val imageRef = storage.reference.child("segunda_planta.png")
        imageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "Imagen cargada exitosamente: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen: ${exception.message}")
            }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Mostrar el fondo de pantalla
        if (backgroundUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(model = backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (loadError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray) // Fondo alternativo si no se carga la imagen
            )
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                imageUrl != null -> {
                    // Mostrar la imagen si se obtuvo la URL
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUrl),
                        contentDescription = "Second Floor",
                        modifier = Modifier
                            .size(300.dp)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offsetX,
                                translationY = offsetY,
                                rotationZ = rotationState
                            )
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, rotation ->
                                    scale *= zoom
                                    rotationState += rotation
                                    offsetX += pan.x
                                    offsetY += pan.y
                                }
                            }
                    )
                }

                loadError -> {
                    // Mensaje de error si hubo un problema al cargar la imagen
                    Text("Error al cargar la imagen. Verifica tu conexión o permisos.")
                }

                else -> {
                    // Mensaje de carga mientras se obtiene la URL
                    Text("Cargando imagen...")
                }
            }

            // Botones de navegación
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("hospitalization_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Hospitalización")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("icu_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "UCI")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("surgery_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Quirófano")
            }
        }
    }
}