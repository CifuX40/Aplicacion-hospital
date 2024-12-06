package com.example.mardeluna.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondFloorScreen(navController: NavHostController = rememberNavController()) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var rotationState by remember { mutableFloatStateOf(0f) }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var backgroundUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la imagen de fondo y la imagen principal desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = Firebase.storage

        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { loadError = true }

        val imageRef = storage.reference.child("segunda_planta.png")
        imageRef.downloadUrl
            .addOnSuccessListener { uri -> imageUrl = uri.toString() }
            .addOnFailureListener { loadError = true }
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
                    IconButton(onClick = { navController.navigate("main_logo") }) {
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
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Fondo de pantalla
                if (backgroundUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(backgroundUrl),
                        contentDescription = "Fondo de pantalla",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (loadError) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray)
                    )
                }

                // Contenido principal
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (imageUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = "Segunda planta",
                            modifier = Modifier
                                .size(300.dp)
                                .graphicsLayer(
                                    scaleX = scale,
                                    scaleY = scale,
                                    translationX = offsetX,
                                    translationY = offsetY,
                                    rotationZ = rotationState
                                )
                                .pointerInput(Unit) {
                                    detectTransformGestures { _, pan, zoom, rotation ->
                                        scale *= zoom
                                        rotationState += rotation
                                        offsetX += pan.x
                                        offsetY += pan.y
                                    }
                                }
                        )
                    } else if (loadError) {
                        Text("Error al cargar la imagen. Verifica tu conexión o permisos.")
                    } else {
                        Text("Cargando imagen...")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botones de navegación
                    Button(
                        onClick = { navController.navigate("hospitalizacion") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Hospitalización")
                    }

                    Button(
                        onClick = { navController.navigate("UCI") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("UCI")
                    }

                    Button(
                        onClick = { navController.navigate("Quirófano") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Quirófano")
                    }
                }
            }
        }
    )
}