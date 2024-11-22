package com.example.mardeluna.view

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
import coil.compose.*
import com.google.firebase.storage.*
import androidx.compose.ui.viewinterop.*
import androidx.compose.ui.layout.*


@Composable
fun HospitalDeDiaScreen(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL de la imagen de fondo desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { Log.e("Firebase", "Error al cargar fondo: ${it.message}") }

        // Cargar video desde Firebase Storage
        val videoRef = storage.reference.child("toma_constantes.mp4")
        videoRef.downloadUrl
            .addOnSuccessListener { uri -> videoUrl = uri.toString() }
            .addOnFailureListener { Log.e("Firebase", "Error al cargar video: ${it.message}") }

        // Cargar imagen específica de la pantalla
        loadImageFromFirebase("hospital_dia.jpg") { url, error ->
            imageUrl = url ?: ""
            loadError = error != null
            error?.let { Log.e("Firebase", "Error al cargar la imagen: ${it.message}") }
        }
    }

    // Estructura principal con fondo
    Box(modifier = Modifier.fillMaxSize()) {
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
                    .background(Color.LightGray)
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
            Text(
                text = "Hospital de Día",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar la imagen cargada desde Firebase Storage
            if (!loadError && imageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Hospital de Día",
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

            // Mostrar el texto con la descripción
            Text(
                text = """
                    El Hospital de Día consta de 7 habitaciones donde ingresarán los pacientes de cirugía menor ambulatoria.
                    La enfermera de Hospital de Día recepcionará a los pacientes del siguiente modo:
                    
                    - Recepción del paciente, acompañamiento a su habitación y resolución de dudas.
                    - Comprobación de nombre, apellidos y pulsera identificativa.
                    - Valoración de enfermería (posibles alergias, medicación habitual, …).
                    - Tomas de constantes.
                    - Recopilación de documentos necesarios para la intervención (preanestesia y consentimientos).
                """.trimIndent(),
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Si hay una URL de video, mostrar el video con controles
            videoUrl?.let {
                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar el video con controles manuales
                AndroidView(
                    factory = { context ->
                        VideoView(context).apply {
                            setVideoURI(Uri.parse(it)) // Enlace de Firebase Storage
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

// Función para cargar imágenes desde Firebase Storage
private fun loadImageFromFirebase(fileName: String, onResult: (String?, Exception?) -> Unit) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child(fileName)
    storageRef.downloadUrl
        .addOnSuccessListener { uri -> onResult(uri.toString(), null) }
        .addOnFailureListener { exception -> onResult(null, exception) }
}