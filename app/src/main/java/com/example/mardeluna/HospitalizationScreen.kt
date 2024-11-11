package com.example.mardeluna

import android.util.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*

@Composable
fun HospitalizationScreen(navController: NavHostController) {
    // Estado para la URL de la imagen
    var imageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL de la imagen desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("habitacion.jpg")

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

    // Envuelve todo el contenido en una columna que permite el desplazamiento vertical
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Habilita el desplazamiento vertical
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Habitaciones de hospitalización.",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.TopStart
        ) {
            // Mostrar la imagen si la URL está cargada y no hay error
            if (imageUrl.isNotEmpty() && !loadError) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Room",
                    modifier = Modifier.fillMaxSize()
                )
            } else if (loadError) {
                Text(
                    text = "Error al cargar la imagen",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // Mostrar un texto de carga o placeholder
                Text(
                    text = "Cargando imagen...",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Todas las habitaciones tienen una aspiración y una toma de oxígeno " +
                    "que se deberán comprobar su funcionamiento después de cada alta de paciente " +
                    "y limpieza de las mismas.",
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Aparataje",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

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