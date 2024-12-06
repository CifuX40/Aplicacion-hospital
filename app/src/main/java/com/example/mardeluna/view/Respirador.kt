package com.example.mardeluna.view

import android.app.*
import android.content.pm.*
import android.net.*
import android.widget.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.*
import androidx.navigation.*
import coil.compose.*

@Composable
fun Respirador(navController: NavHostController) {
    val context = LocalContext.current
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL del fondo desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = com.google.firebase.storage.FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("fondo_de_pantalla.jpg")
        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundUrl = uri.toString()
            }
            .addOnFailureListener { exception ->
                loadError = true
                android.util.Log.e("Firebase", "Error al cargar el fondo: ${exception.message}")
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        if (backgroundUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (loadError) {
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Respirador\nSavina 600",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen del Respirador
            Image(
                painter = rememberAsyncImagePainter("https://firebasestorage.googleapis.com/v0/b/mar-de-luna-ada79.appspot.com/o/respirador.jpg?alt=media"),
                contentDescription = "Imagen del Respirador Savina 300",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Reproducir el video desde Firebase Storage
            AndroidView(
                factory = { context ->
                    VideoView(context).apply {
                        val videoUrl =
                            "https://firebasestorage.googleapis.com/v0/b/mar-de-luna-ada79.appspot.com/o/prueba_respirador.mp4?alt=media"
                        setVideoURI(Uri.parse(videoUrl))

                        // Agregar controles al VideoView
                        val mediaController = MediaController(context)
                        mediaController.setAnchorView(this)
                        setMediaController(mediaController)

                        // Cambiar la orientación a horizontal cuando el video está en pantalla completa
                        setOnPreparedListener {
                            setOnInfoListener { _, what, _ ->
                                if (what == android.media.MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                    val activity = context as? Activity
                                    activity?.requestedOrientation =
                                        ActivityInfo.SCREEN_ORIENTATION_SENSOR
                                }
                                false
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
    }
}