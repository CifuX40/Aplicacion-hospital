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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*

@Composable
fun Habitacion(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar URLs desde Firebase Storage
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
        val imageRef = storage.reference.child("habitacion.jpg")
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

    // Contenedor principal con fondo
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

        Column(modifier = Modifier.fillMaxSize()) {
            // TabBar azul en la parte superior
            TabRow(selectedTabIndex = 0, containerColor = Color.Blue) {
                Tab(selected = true, onClick = {}) {
                    Text(
                        text = "Tab 1",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Tab(selected = false, onClick = {}) {
                    Text(
                        text = "Tab 2",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Título "Habitación" en negrita centrado
            Text(
                text = "Habitación",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen principal
                Box(
                    modifier = Modifier.size(300.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    if (imageUrl.isNotEmpty() && !loadError) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = "Imagen de la habitación",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (loadError) {
                        Text(
                            text = "Error al cargar la imagen",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        Text(
                            text = "Cargando imagen...",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Descripción de las habitaciones
                Text(
                    text = "Todas las habitaciones tienen una aspiración y una toma de oxígeno " +
                            "que se deberán comprobar su funcionamiento después de cada alta de paciente " +
                            "y limpieza de las mismas.",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Título "Aparataje" en negrita
                Text(
                    text = "Aparataje",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botones para navegar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { navController.navigate("aspiracion_screen") }) {
                        Text(text = "Aspiración")
                    }

                    Button(onClick = { navController.navigate("toma_oxigeno_screen") }) {
                        Text(text = "Toma de oxígeno")
                    }
                }
            }
        }
    }
}