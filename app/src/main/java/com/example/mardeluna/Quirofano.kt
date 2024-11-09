package com.example.mardeluna

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.ktx.*
import com.google.firebase.storage.ktx.storage

@Composable
fun SurgeryScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quirófano", fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        // Cargar imágenes desde Firebase Storage
        FirebaseImage("gs://mar-de-luna-ada79.firebasestorage.app/rea.jpg", "Área de recuperación")
        FirebaseImage("gs://mar-de-luna-ada79.firebasestorage.app/sala_quirofano.jpg", "Sala de quirófano")
        FirebaseImage("gs://mar-de-luna-ada79.firebasestorage.app/esterilizacion.jpg", "Área de esterilización")
    }
}

@Composable
fun FirebaseImage(storageUrl: String, description: String) {
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL de Firebase Storage
    LaunchedEffect(storageUrl) {
        val storage = Firebase.storage
        val storageRef = storage.getReferenceFromUrl(storageUrl)
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        when {
            imageUrl != null -> {
                // Mostrar la imagen si se obtuvo la URL correctamente
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = description,
                    modifier = Modifier.size(200.dp)
                )
            }
            loadError -> {
                Text("Error al cargar la imagen: $description")
            }
            else -> {
                Text("Cargando imagen...")
            }
        }

        Text(text = description, modifier = Modifier.padding(8.dp))
    }
}