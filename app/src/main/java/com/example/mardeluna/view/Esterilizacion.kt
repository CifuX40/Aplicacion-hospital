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
fun EsterilizacionScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }

    // Cargar fondo desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = Firebase.storage
        val backgroundRef = storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception -> Log.e("Firebase", "Error al cargar fondo: ${exception.message}") }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (backgroundUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Esterilización",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}