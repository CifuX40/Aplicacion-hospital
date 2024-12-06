package com.example.mardeluna.view

import android.util.*
import android.widget.*
import android.net.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*

@Composable
fun Desfibrilador(navController: NavHostController) {
    var backgroundImageUrl by remember { mutableStateOf<String?>(null) }
    var videoUrl by remember { mutableStateOf<String?>(null) }
    var listadoCarroParadasUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Descargar la URL de la imagen de fondo
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo
        val fondoRef = storage.reference.child("fondo_de_pantalla.jpg")
        fondoRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundImageUrl = uri.toString()
                Log.d("Firebase", "URL de fondo obtenida: $backgroundImageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al obtener URL del fondo: ${exception.message}")
            }

        // Cargar video
        val videoRef = storage.reference.child("desfibrilador_UCI.mp4")
        videoRef.downloadUrl
            .addOnSuccessListener { uri ->
                videoUrl = uri.toString()
                Log.d("Firebase", "URL del video obtenida: $videoUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al obtener URL del video: ${exception.message}")
            }

        // Cargar imagen listado_carro_paradas.jpg
        val listadoRef = storage.reference.child("listado_carro_paradas.jpg")
        listadoRef.downloadUrl
            .addOnSuccessListener { uri ->
                listadoCarroParadasUrl = uri.toString()
                Log.d("Firebase", "URL de listado_carro_paradas obtenida: $listadoCarroParadasUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al obtener URL de listado_carro_paradas: ${exception.message}")
            }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        if (backgroundImageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(model = backgroundImageUrl),
                contentDescription = "Fondo de pantalla",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else if (loadError) {
            Text(
                text = "Error al cargar el fondo",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Text(
                text = "Cargando fondo...",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Desfibrilador",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Video de Firebase (solo si la URL está disponible)
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
                            start()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )
            }

            // Imagen listado_carro_paradas debajo del video
            listadoCarroParadasUrl?.let {
                Image(
                    painter = rememberAsyncImagePainter(model = it),
                    contentDescription = "Listado de carro de paradas",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
