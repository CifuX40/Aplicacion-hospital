package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Uci(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar imágenes desde Firebase
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar el fondo de pantalla
        storage.reference.child("fondo_de_pantalla.jpg").downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar el fondo: ${exception.message}")
            }

        // Cargar la imagen principal
        storage.reference.child("uci.jpg").downloadUrl
            .addOnSuccessListener { uri -> imageUrl = uri.toString() }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen: ${exception.message}")
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Mostrar el fondo de pantalla
            if (backgroundUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(model = backgroundUrl),
                    contentDescription = "Fondo de pantalla",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (loadError) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Unidad de Cuidados Intensivos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!loadError && imageUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Sala UCI",
                        modifier = Modifier.size(300.dp)
                    )
                } else {
                    Text(text = "Error al cargar la imagen", color = Color.Red)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Información general del Servicio de UCI\n\n" +
                            "Promovemos acciones para brindar atención más humana y amable. Horarios de visitas:\n\n" +
                            "• Turno MAÑANA: 12:00 a 14:00\n" +
                            "• Turno TARDE: 16:00 a 19:00\n\n" +
                            "Consulte con la enfermera para horarios flexibles según las necesidades del paciente y familiares. Es importante mantener el tono bajo, lavarse las manos al entrar y salir, y dejar un teléfono de contacto para notificaciones importantes. Las visitas son limitadas a dos personas por habitación, con posibilidad de intercambio fuera de la unidad.",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("uci_postquirurgica") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = "UCI Postquirúrgica")
                }

                Button(
                    onClick = { navController.navigate("uci_medica") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = "UCI Médica")
                }
            }
        }
    }
}