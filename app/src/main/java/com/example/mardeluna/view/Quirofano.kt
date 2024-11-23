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
import androidx.compose.ui.layout.*
import coil.compose.*
import com.google.firebase.ktx.*
import com.google.firebase.storage.ktx.*

@Composable
fun SurgeryScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var surgeryImageUrl by remember { mutableStateOf("") }

    // Cargar fondo desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = Firebase.storage

        // Fondo
        val backgroundRef = storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception -> Log.e("Firebase", "Error al cargar fondo: ${exception.message}") }

        // Imagen de quirófano
        val surgeryImageRef = storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/quirofano.jpg")
        surgeryImageRef.downloadUrl
            .addOnSuccessListener { uri -> surgeryImageUrl = uri.toString() }
            .addOnFailureListener { exception -> Log.e("Firebase", "Error al cargar imagen quirófano: ${exception.message}") }
    }

    // Contenedor con fondo
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla
        if (backgroundUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
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
            // Título
            Text(
                text = "Quirófano",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Imagen de quirófano
            if (surgeryImageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(surgeryImageUrl),
                    contentDescription = "Imagen de quirófano",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(bottom = 16.dp)
                )
            }

            // Botones
            Button(
                onClick = { /* Navegar a Esterilización */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Esterilización")
            }

            Button(
                onClick = { /* Navegar a REA */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Rea")
            }

            Button(
                onClick = { /* Navegar a Sala quirófano */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Sala quirófano")
            }
        }
    }
}