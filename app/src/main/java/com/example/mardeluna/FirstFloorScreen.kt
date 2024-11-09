package com.example.mardeluna

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun FirstFloorScreen(navController: NavHostController = rememberNavController()) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var rotationState by remember { mutableFloatStateOf(1f) }

    val imageUrl = "gs://mar-de-luna-ada79.firebasestorage.app/primera_planta.png" // URL de Firebase Storage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cargar la imagen desde Firebase Storage usando Coil
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
        Spacer(modifier = Modifier.height(16.dp))
    }
}