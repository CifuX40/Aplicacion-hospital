package com.example.mardeluna

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.FirebaseStorage

@Composable
fun MainLogoScreen(navController: NavHostController) {
    var firstFloorLogoUrl by remember { mutableStateOf("") }
    var secondFloorLogoUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar imagen de la primera planta
        val firstFloorRef = storage.reference.child("piso_1_logo.png")
        firstFloorRef.downloadUrl
            .addOnSuccessListener { uri ->
                firstFloorLogoUrl = uri.toString()
                Log.d("Firebase", "Imagen cargada exitosamente para la primera planta: $firstFloorLogoUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen de la primera planta: ${exception.message}")
            }

        // Cargar imagen de la segunda planta
        val secondFloorRef = storage.reference.child("piso_2_logo.png")
        secondFloorRef.downloadUrl
            .addOnSuccessListener { uri ->
                secondFloorLogoUrl = uri.toString()
                Log.d("Firebase", "Imagen cargada exitosamente para la segunda planta: $secondFloorLogoUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen de la segunda planta: ${exception.message}")
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!loadError) {
            // Imagen de la primera planta desde Firebase Storage
            if (firstFloorLogoUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(firstFloorLogoUrl),
                    contentDescription = "Logo de la Primera Planta",
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { navController.navigate("first_floor") }
                        .padding(bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen de la segunda planta desde Firebase Storage
            if (secondFloorLogoUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(secondFloorLogoUrl),
                    contentDescription = "Logo de la Segunda Planta",
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { navController.navigate("second_floor") }
                        .padding(bottom = 16.dp)
                )
            }
        } else {
            // Mostrar un mensaje de error si la carga falló
            Text(text = "Error al cargar las imágenes", color = Color.Red)
        }
    }
}
