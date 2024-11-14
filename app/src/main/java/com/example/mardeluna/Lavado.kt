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
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun Lavado(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL de la imagen y del video desde Firebase Storage
    LaunchedEffect(Unit) {
        loadUrlFromFirebase("lavadora_endoscopias.jpg") { url, error ->
            imageUrl = url ?: ""
            loadError = error != null
            error?.let { Log.e("Firebase", "Error al cargar la imagen: ${it.message}") }
        }

        loadUrlFromFirebase("lavadora_de_endoscopias.mp4") { url, error ->
            videoUrl = url ?: ""
            loadError = loadError || (error != null)
            error?.let { Log.e("Firebase", "Error al cargar el video: ${it.message}") }
        }
    }

    // Añadimos el scroll vertical en la columna principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Lavadora de endoscopias",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar la imagen cargada desde Firebase Storage
        if (!loadError && imageUrl.isNotEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Lavadora de endoscopias",
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

        // Mostrar el reproductor de video si la URL se cargó correctamente
        if (!loadError && videoUrl.isNotEmpty()) {
            VideoPlayer(videoUrl)
        } else if (loadError) {
            Text(
                text = "Error al cargar el video",
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
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

@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}