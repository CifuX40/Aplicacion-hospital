package com.example.mardeluna.view

import android.util.*
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
import androidx.compose.ui.layout.*

@Composable
fun UciMedicaScreen(navController: NavHostController) {
    var backgroundImageUrl by remember { mutableStateOf<String?>(null) }
    var salaImageUrl by remember { mutableStateOf<String?>(null) }
    var criteriosImageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Descargar URLs de imágenes
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo
        val fondoRef = storage.reference.child("fondo_de_pantalla.jpg")
        fondoRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundImageUrl = uri.toString() }
            .addOnFailureListener { loadError = true }

        // Cargar imagen de la sala de UCI
        val salaRef = storage.reference.child("sala_uci_uno.jpg")
        salaRef.downloadUrl
            .addOnSuccessListener { uri -> salaImageUrl = uri.toString() }
            .addOnFailureListener { loadError = true }

        // Cargar imagen de criterios de admisión
        val criteriosRef = storage.reference.child("criterios_admision_uci.jpg")
        criteriosRef.downloadUrl
            .addOnSuccessListener { uri -> criteriosImageUrl = uri.toString() }
            .addOnFailureListener { loadError = true }
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "UCI Médica",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Imagen de la sala de UCI
            salaImageUrl?.let {
                Image(
                    painter = rememberAsyncImagePainter(model = it),
                    contentDescription = "Sala de UCI",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // Texto: Criterios de admisión
            Text(
                text = "Criterios de admisión en UCI Médica",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "La definición dada a la UCI delimita los dos criterios clave para los pacientes en la Unidad:\n" +
                        "- Que precisen un elevado nivel de cuidados.\n" +
                        "- Que sean recuperables.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Imagen de criterios de admisión
            criteriosImageUrl?.let {
                Image(
                    painter = rememberAsyncImagePainter(model = it),
                    contentDescription = "Criterios de admisión en UCI",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // Botones de navegación
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("desfibrilador_screen") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Desfibrilador")
                }

                Button(
                    onClick = { navController.navigate("evita_300_screen") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Evita 600")
                }
            }
        }
    }
}