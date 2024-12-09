package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.compose.ui.layout.*
import coil.compose.*
import com.google.firebase.ktx.*
import com.google.firebase.storage.ktx.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rea(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var reaImageUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val storage = Firebase.storage

        val backgroundRef =
            storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception ->
                Log.e(
                    "Firebase",
                    "Error al cargar fondo: ${exception.message}"
                )
            }

        val reaRef =
            storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/rea.jpg")
        reaRef.downloadUrl
            .addOnSuccessListener { uri -> reaImageUrl = uri.toString() }
            .addOnFailureListener { exception ->
                Log.e(
                    "Firebase",
                    "Error al cargar imagen REA: ${exception.message}"
                )
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mar de Luna",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Plantas") }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if (backgroundUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(backgroundUrl),
                    contentDescription = "Fondo de pantalla",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "REA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (reaImageUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(reaImageUrl),
                        contentDescription = "Imagen de la REA",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(bottom = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = "El paciente será trasladado a la REA o zona de despertar para seguimiento y estabilización antes de remitirlo a la planta de destino, o bien se le dé el alta (pacientes ambulatorios).",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Seguimiento y alta del paciente en el despertar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Cuando el paciente llegue al despertar se monitorizará y será controlado por el anestesista y/o personal de enfermería del despertar/REA, aunque el responsable del paciente será el anestesista. Cuando considere que el paciente está lo suficientemente estable para el traslado a la unidad de destino lo comunicará al equipo para que se traslade al paciente.",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}