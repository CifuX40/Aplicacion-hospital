package com.example.mardeluna

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.storage.FirebaseStorage

@Composable
fun Endoscopios(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL de la imagen desde Firebase Storage
    LaunchedEffect(Unit) {
        loadUrlFromFirebase("endoscopios.jpg") { url, error ->
            imageUrl = url ?: ""
            loadError = error != null
            error?.let { Log.e("Firebase", "Error al cargar la imagen: ${it.message}") }
        }
    }

    // Habilitar desplazamiento vertical
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Scroll vertical habilitado
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Endoscopios",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar la imagen cargada desde Firebase Storage
        if (!loadError && imageUrl.isNotEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Imagen de endoscopios",
                modifier = Modifier
                    .size(300.dp)
                    .padding(8.dp)
            )
        } else if (loadError) {
            Text(
                text = "Error al cargar la imagen",
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción del endoscopio
        Text(
            text = "El endoscopio es un instrumento en forma de tubo semiflexible que contiene una luz " +
                    "y una óptica que permiten la visualización del interior de una cavidad corporal.\n" +
                    "La endoscopia en el aparato digestivo se divide en:\n" +
                    "- Gastroscopia (endoscopio oral o gastroscopio)\n" +
                    "- Colonoscopia (colonoscopio)",
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

// Función para cargar URLs desde Firebase Storage
private fun loadUrlFromFirebase(fileName: String, onResult: (String?, Exception?) -> Unit) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child(fileName)
    storageRef.downloadUrl
        .addOnSuccessListener { uri -> onResult(uri.toString(), null) }
        .addOnFailureListener { exception -> onResult(null, exception) }
}