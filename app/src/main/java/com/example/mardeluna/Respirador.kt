package com.example.mardeluna

import android.app.*
import android.content.pm.*
import android.net.*
import android.widget.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.*
import coil.compose.rememberAsyncImagePainter

@Composable
fun RespiradorScreen(navController: NavHostController) {
    val context = LocalContext.current

    // Estado de scroll para habilitar desplazamiento vertical
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Habilita el scroll vertical
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Respirador\nSavina 300",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen del Respirador desde Firebase Storage
        Image(
            painter = rememberAsyncImagePainter("https://firebasestorage.googleapis.com/v0/b/mar-de-luna-ada79.appspot.com/o/respirador.jpg?alt=media"),
            contentDescription = "Imagen del Respirador Savina 300",
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para ver guía en YouTube
        Button(
            onClick = {
                val intent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    Uri.parse("https://youtu.be/W51JlmJjPrc?si=hLXu81Fo2-6QVI43")
                )
                context.startActivity(intent)
            }
        ) {
            Text(text = "Guía Savina 300")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reproducir el video desde Firebase Storage
        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    // URL pública del video desde Firebase Storage
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
                                    ActivityInfo.SCREEN_ORIENTATION_SENSOR // Permitir rotación automática
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