package com.example.mardeluna.view

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
fun MainLogoScreen(navController: NavHostController) {
    var firstFloorLogoUrl by remember { mutableStateOf("") }
    var secondFloorLogoUrl by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar imagen de fondo
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { loadError = true }

        // Cargar imagen de la primera planta
        val firstFloorRef = storage.reference.child("piso_1_logo.png")
        firstFloorRef.downloadUrl
            .addOnSuccessListener { uri -> firstFloorLogoUrl = uri.toString() }
            .addOnFailureListener { loadError = true }

        // Cargar imagen de la segunda planta
        val secondFloorRef = storage.reference.child("piso_2_logo.png")
        secondFloorRef.downloadUrl
            .addOnSuccessListener { uri -> secondFloorLogoUrl = uri.toString() }
            .addOnFailureListener { loadError = true }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla
        if (backgroundUrl.isNotEmpty() && !loadError) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (loadError) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Gray))
        }

        // Contenido de la pantalla
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!loadError) {
                Text(
                    text = "Plantas",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

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

                Spacer(modifier = Modifier.height(24.dp))

                // Botón para ir a Publicaciones
                Button(
                    onClick = { navController.navigate("PublicacionesScreen") },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Ir a publicaciones")
                }
            } else {
                // Mostrar un mensaje de error si la carga falló
                Text(text = "Error al cargar las imágenes", color = Color.Red)
            }

            // Botón de cerrar sesión
            Button(
                onClick = {
                    navController.navigate("start") {
                        popUpTo("start") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}