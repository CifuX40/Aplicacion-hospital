package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*
import androidx.compose.ui.layout.*

@Composable
fun UciMedicaScreen(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Descargar la URL de la imagen de Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("fondo_de_pantalla.jpg")
        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "URL de fondo obtenida: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al obtener URL del fondo: ${exception.message}")
            }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "Fondo de pantalla",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else if (loadError) {
            Text(
                text = "Error al cargar el fondo",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Text(
                text = "Cargando fondo...",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "UCI Médica",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
