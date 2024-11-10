package com.example.mardeluna

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.*

@Composable
fun FirstFloorScreen(navController: NavHostController = rememberNavController()) {
    // Variables de estado para transformar la imagen
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var rotationState by remember { mutableFloatStateOf(0f) }

    // Variables de estado para almacenar la URL de la imagen y el estado de carga
    var imageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL de la imagen desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("primera_planta.png")

        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "Imagen de la primera planta cargada exitosamente: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e(
                    "Firebase",
                    "Error al cargar la imagen de la primera planta: ${exception.message}"
                )
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Verificar si la imagen se cargó correctamente y mostrarla
        if (imageUrl.isNotEmpty() && !loadError) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Imagen de la primera planta",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
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
        } else if (loadError) {
            Text(
                text = "Error al cargar la imagen",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            Text(
                text = "Cargando imagen...",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones debajo de la imagen
        Button(
            onClick = { navController.navigate("endoscopias_screen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Endoscopias")
        }

        Button(
            onClick = { navController.navigate("hospital_dia_screen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Hospital de Día")
        }

        Button(
            onClick = { navController.navigate("hospitalization_screen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Hospitalización")
        }
    }
}


//agregar los botones funcionales: Endoscopias, Hospital de Dia, Hospitalizacion