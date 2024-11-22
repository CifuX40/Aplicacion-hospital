package com.example.mardeluna.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.FirebaseStorage
import androidx.navigation.NavHostController
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.Composable

@Composable
fun CarroIngresosScreen(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Descargar la URL de la imagen de fondo
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
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
                text = "Carro de ingresos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}