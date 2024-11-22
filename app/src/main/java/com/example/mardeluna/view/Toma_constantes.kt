package com.example.mardeluna.view

import android.net.*
import android.widget.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*

@Composable
fun TomaConstantesScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf<String?>(null) }

    // Cargar la URL de la imagen de fondo y el video desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { /* Manejar error */ }

        val videoRef = storage.reference.child("toma_constantes.mp4")
        videoRef.downloadUrl
            .addOnSuccessListener { uri -> videoUrl = uri.toString() }
            .addOnFailureListener { /* Manejar error */ }
    }

    // Fondo y contenido principal
    Box(modifier = Modifier.fillMaxSize()) {
        if (backgroundUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
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
                text = "Toma de Constantes",
                fontSize = 24.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            videoUrl?.let {
                AndroidView(
                    factory = { context ->
                        VideoView(context).apply {
                            setVideoURI(Uri.parse(it))
                            val mediaController = MediaController(context).apply {
                                setAnchorView(this@apply)
                            }
                            setMediaController(mediaController)
                            requestFocus()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }
        }
    }
}
