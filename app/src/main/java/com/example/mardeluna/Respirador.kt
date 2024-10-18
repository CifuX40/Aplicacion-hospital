package com.example.mardeluna

import android.content.Intent
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController

@Composable
fun RespiradorScreen(navController: NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/W51JlmJjPrc?si=hLXu81Fo2-6QVI43"))
                context.startActivity(intent)
            }
        ) {
            Text(text = "Guía Savina 300")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para reproducir el video local
        Button(
            onClick = {
                // Aquí mostramos el VideoView dentro de la composición
            }
        ) {
            Text(text = "Reproducir Video Local")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reproducir el video local utilizando VideoView
        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    val videoPath = "android.resource://" + context.packageName + "/" + R.raw.prueba_respirador
                    setVideoURI(Uri.parse(videoPath))

                    // Agregar controles al VideoView
                    val mediaController = MediaController(context)
                    mediaController.setAnchorView(this)
                    setMediaController(mediaController)

                    // Iniciar la reproducción automática
                    start()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Altura del VideoView
        )
    }
}