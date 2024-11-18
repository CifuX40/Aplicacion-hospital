package com.example.mardeluna

import android.net.*
import android.util.*
import android.widget.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import com.google.firebase.storage.*
import androidx.compose.ui.viewinterop.*
import coil.compose.*
import androidx.compose.ui.layout.*

@Composable
fun ResiduosHospitalariosScreen(navController: NavHostController) {
    var videoUrl by remember { mutableStateOf<String?>(null) }
    var backgroundUrl by remember { mutableStateOf("") }

    // Cargar fondo de pantalla desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { Log.e("Firebase", "Error al cargar fondo: ${it.message}") }

        // Cargar video desde Firebase Storage
        val videoRef = storage.reference.child("residuos.mp4")
        videoRef.downloadUrl
            .addOnSuccessListener { uri -> videoUrl = uri.toString() }
            .addOnFailureListener { Log.e("Firebase", "Error al cargar video: ${it.message}") }
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
                text = "Residuos hospitalarios",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Si hay una URL de video, mostrar el video
            videoUrl?.let {
                Spacer(modifier = Modifier.height(16.dp))

                // Reproducir el video
                AndroidView(
                    factory = { context ->
                        VideoView(context).apply {
                            setVideoURI(Uri.parse(it))
                            setOnPreparedListener { mediaPlayer ->
                                mediaPlayer.isLooping = true
                                start()
                            }
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