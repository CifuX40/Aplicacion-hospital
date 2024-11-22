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
fun HospitalDeDiaScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar recursos desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo
        storage.reference.child("fondo_de_pantalla.jpg").downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { Log.e("Firebase", "Error al cargar fondo: ${it.message}") }

        // Cargar imagen
        loadImageFromFirebase("hospital_dia.jpg") { url, error ->
            imageUrl = url ?: ""
            loadError = error != null
            error?.let { Log.e("Firebase", "Error al cargar la imagen: ${it.message}") }
        }
    }

    // Contenedor principal
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla
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
                .verticalScroll(rememberScrollState()) // Habilitar desplazamiento vertical
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hospital de Día",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Botón "Toma de constantes"
            Button(onClick = { navController.navigate("Toma_constantes") }) {
                Text(text = "Toma de constantes")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen descriptiva
            if (!loadError && imageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Hospital de Día",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp)
                )
            } else if (loadError) {
                Text(
                    text = "Error al cargar la imagen",
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

// Función para cargar imágenes desde Firebase Storage
fun loadImageFromFirebase(fileName: String, onResult: (String?, Exception?) -> Unit) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child(fileName)
    storageRef.downloadUrl
        .addOnSuccessListener { uri -> onResult(uri.toString(), null) }
        .addOnFailureListener { exception -> onResult(null, exception) }
}