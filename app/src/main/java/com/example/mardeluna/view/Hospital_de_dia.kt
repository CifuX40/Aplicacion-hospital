package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*

@Composable
fun HospitalDeDiaScreen(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL de la imagen de fondo desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { Log.e("Firebase", "Error al cargar fondo: ${it.message}") }

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

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para navegación a la pantalla de Toma de Constantes
            Button(onClick = { navController.navigate("Toma_constantes") }) {
                Text(text = "Toma de constantes")
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