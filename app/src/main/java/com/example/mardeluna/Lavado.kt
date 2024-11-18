package com.example.mardeluna

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*
import androidx.compose.ui.platform.*
import androidx.media3.common.*
import androidx.media3.exoplayer.*
import androidx.media3.ui.*
import androidx.compose.ui.viewinterop.*

@Composable
fun Lavado(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar las URLs de la imagen de fondo, imagen principal y video desde Firebase Storage
    LaunchedEffect(Unit) {
        loadUrlFromFirebase("fondo_de_pantalla.jpg") { url, error ->
            backgroundUrl = url ?: ""
            loadError = error != null
            error?.let { Log.e("Firebase", "Error al cargar el fondo: ${it.message}") }
        }

        loadUrlFromFirebase("lavadora_endoscopias.jpg") { url, error ->
            imageUrl = url ?: ""
            loadError = loadError || (error != null)
            error?.let { Log.e("Firebase", "Error al cargar la imagen: ${it.message}") }
        }

        loadUrlFromFirebase("lavadora_de_endoscopias.mp4") { url, error ->
            videoUrl = url ?: ""
            loadError = loadError || (error != null)
            error?.let { Log.e("Firebase", "Error al cargar el video: ${it.message}") }
        }
    }

    // Crear un contenedor con el fondo de pantalla
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        if (backgroundUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray) // Color de fondo provisional
            )
        }

        // Contenido principal
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
