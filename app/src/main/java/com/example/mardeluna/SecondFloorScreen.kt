package com.example.mardeluna

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@Composable
fun SecondFloorScreen(navController: NavHostController = rememberNavController()) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var rotationState by remember { mutableStateOf(1f) }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Obtener la URL de descarga de Firebase Storage al inicio
    LaunchedEffect(Unit) {
        val storage = Firebase.storage
        // Cambiamos el path para que apunte a la URL especificada
        val storageRef = storage.reference.child("segunda_planta.png")
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
