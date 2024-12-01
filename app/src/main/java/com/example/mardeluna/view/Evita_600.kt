package com.example.mardeluna.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.FirebaseStorage

@Composable
fun Evita600Screen(navController: NavHostController) {
    var backgroundImageUrl by remember { mutableStateOf<String?>(null) }
    var evita600ImageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Descargar la URL de la imagen de fondo y evita_600.jpg
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

        // Cargar imagen evita_600.jpg
        val evita600Ref = storage.reference.child("evita_600.jpg")
        evita600Ref.downloadUrl
            .addOnSuccessListener { uri ->
                evita600ImageUrl = uri.toString()
                Log.d("Firebase", "URL de evita_600 obtenida: $evita600ImageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al obtener URL de evita_600: ${exception.message}")
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

        // Contenido principal desplazable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Habilitar el desplazamiento vertical
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Evita 600",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Imagen evita_600.jpg debajo del título
            evita600ImageUrl?.let {
                Image(
                    painter = rememberAsyncImagePainter(model = it),
                    contentDescription = "Evita 600",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(500.dp), // Aumentar la altura
                    contentScale = ContentScale.FillHeight // Ajuste para que ocupe toda la altura
                )
            }

            // Texto adicional
            Column(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Ventilación pulmonar protectora para adultos, niños y neonatos, tanto de tipo invasivo, no invasivo como con terapia de O2",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Funciones avanzadas de monitorización pulmonar y diagnóstico (p. ej. Smart Pulmonary View o maniobra de flujo lento)",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Visualización de la distribución regional de la ventilación con PulmoVista® 500",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Herramientas de reclutamiento (p. ej. QuickSet® y PressureLink) y apoyo para la toma de decisiones terapéuticas mediante las tendencias de la respiración en tiempo real (PEEP, EIP, Vt, Cdin)",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Énfasis en el volumen pulmonar espiratorio final, mediante PC-APRV con la función AutoRelease®",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Monitorización volumétrica de CO2 (VCO2, VTCO2, Pendiente de la Fase 3, Vds/VTe)",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}