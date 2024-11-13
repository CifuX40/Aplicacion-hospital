package com.example.mardeluna

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import com.google.firebase.storage.FirebaseStorage

@Composable
fun Endoscopias(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL de la imagen desde Firebase Storage
    LaunchedEffect(Unit) {
        loadImageFromFirebase { url, error ->
            imageUrl = url ?: ""
            loadError = error != null
            error?.let { Log.e("Firebase", "Error al cargar la imagen: ${it.message}") }
        }
    }

    // Configuración de desplazamiento horizontal y contenido
    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Content(navController, loadError)
    }
}

// Encapsulando la lógica de carga de imagen
private fun loadImageFromFirebase(onResult: (String?, Exception?) -> Unit) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child("endoscopias_camilla.jpg")
    storageRef.downloadUrl
        .addOnSuccessListener { uri -> onResult(uri.toString(), null) }
        .addOnFailureListener { exception -> onResult(null, exception) }
}

@Composable
private fun Content(navController: NavHostController, loadError: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sala endoscopias",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (loadError) {
            Text(
                text = "Error al cargar la imagen",
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Botones de navegación
        NavigationButtons(navController)
    }
}

@Composable
private fun NavigationButtons(navController: NavHostController) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(onClick = { navController.navigate("endoscopios_screen") }) {
            Text("Endoscopios")
        }

        Button(onClick = { navController.navigate("lavado_screen") }) {
            Text("Lavado")
        }
    }
}