package com.example.mardeluna

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController

@Composable
fun RespiradorScreen(navController: NavHostController) {
    val context = LocalContext.current

    // Scroll state para habilitar desplazamiento vertical
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

        Image(
            painter = painterResource(id = R.drawable.respirador),
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

        // Reproducir el video local utilizando VideoView
        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    val videoPath =
                        "android.resource://" + context.packageName + "/" + R.raw.prueba_respirador
                    setVideoURI(Uri.parse(videoPath))

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
                .height(300.dp) // Altura del VideoView
        )
    }
}