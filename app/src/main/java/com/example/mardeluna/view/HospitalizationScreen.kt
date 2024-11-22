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
fun HospitalizationScreen(navController: NavHostController) {
    // Estado para la URL de la imagen
    var imageUrl by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL de la imagen del fondo desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo de pantalla
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { Log.e("Firebase", "Error al cargar fondo: ${it.message}") }

        // Cargar imagen específica de la pantalla
        val storageRef = storage.reference.child("hospitalizacion.jpg")
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

    // Estructura principal con fondo
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
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

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Habitaciones de hospitalización.",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.size(300.dp),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl.isNotEmpty() && !loadError) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Room",
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (loadError) {
                    Text(
                        text = "Error al cargar la imagen",
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Text(text = "Cargando imagen...")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Todas las habitaciones tienen una aspiración y una toma de oxígeno " +
                        "que se deberán comprobar su funcionamiento después de cada alta de paciente " +
                        "y limpieza de las mismas.",
                fontSize = 16.sp,
                modifier = Modifier.padding(8.dp),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Aparataje",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones dispuestos uno debajo del otro
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("carro_paradas") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Carro de paradas")
                }

                Button(
                    onClick = { navController.navigate("control_enfermeria") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Control de enfermería")
                }

                Button(
                    onClick = { navController.navigate("room_screen") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Habitación")
                }
            }
        }
    }
}