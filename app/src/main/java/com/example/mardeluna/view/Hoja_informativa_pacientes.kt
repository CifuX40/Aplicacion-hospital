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
import android.util.*
import coil.compose.*
import com.google.firebase.storage.*

@Composable
fun HojaInformativaPacientesScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }

    // Cargar la URL de la imagen de fondo desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo
        val backgroundRef = storage.reference.child("hoja_informativa_pacientes.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { Log.e("Firebase", "Error al cargar fondo: ${it.message}") }
    }

    // Estructura principal con fondo
    Box(modifier = Modifier.fillMaxSize()) {
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hoja Informativa Pacientes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}