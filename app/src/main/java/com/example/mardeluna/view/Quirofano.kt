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
fun Quirofano(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var surgeryImageUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val storage = Firebase.storage

        val backgroundRef = storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception -> Log.e("Firebase", "Error al cargar fondo: ${exception.message}") }

        val surgeryImageRef = storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/quirofano.jpg")
        surgeryImageRef.downloadUrl
            .addOnSuccessListener { uri -> surgeryImageUrl = uri.toString() }
            .addOnFailureListener { exception -> Log.e("Firebase", "Error al cargar imagen quirófano: ${exception.message}") }
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Quirófano",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

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

            Button(
                onClick = { navController.navigate("esterilizacion") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Esterilización")
            }

            Button(
                onClick = { navController.navigate("rea") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Rea")
            }

            Button(
                onClick = { navController.navigate("sala_quirofano") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Sala quirófano")
            }
        }
    }
}