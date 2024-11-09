package com.example.mardeluna

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.*

@Composable
fun CarinaScreen(navController: NavHostController) {
    // Variables de estado para almacenar la URL de la imagen y el estado de carga
    var imageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL de la imagen desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("carina.jpg")

        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "Imagen de Carina cargada exitosamente: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen de Carina: ${exception.message}")
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título "Carina"
        Text(
            text = "Carina",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar la imagen si la URL está cargada y no hay error
        if (imageUrl.isNotEmpty() && !loadError) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Imagen de Carina",
                modifier = Modifier.size(300.dp)
            )
        } else if (loadError) {
            Text(
                text = "Error al cargar la imagen",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            Text(
                text = "Cargando imagen...",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
