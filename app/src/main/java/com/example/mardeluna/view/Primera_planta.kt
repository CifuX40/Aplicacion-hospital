package com.example.mardeluna.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.navigation.compose.*
import coil.compose.*
import com.google.firebase.storage.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimeraPlanta(navController: NavHostController = rememberNavController()) {

    // Variables de estado para almacenar las URLs de las imágenes y el estado de carga
    var backgroundUrl by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar las imágenes desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar la imagen de fondo
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { loadError = true }

        // Cargar la imagen de la primera planta
        val storageRef = storage.reference.child("primera_planta.png")
        storageRef.downloadUrl
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
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Fondo de pantalla
                if (backgroundUrl.isNotEmpty() && !loadError) {
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

                // Contenedor con Scroll
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Primera planta",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Verificar si la imagen de la primera planta se cargó correctamente y mostrarla
                    if (imageUrl.isNotEmpty() && !loadError) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = "Imagen de la primera planta",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        )
                    } else if (loadError) {
                        Text(
                            text = "Error al cargar la imagen",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        Text(
                            text = "Cargando imagen...",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botones debajo de la imagen
                    Button(
                        onClick = { navController.navigate("sala_endoscopias") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Endoscopias")
                    }

                    Button(
                        onClick = { navController.navigate("hospital_dia") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Hospital de Día")
                    }

                    Button(
                        onClick = { navController.navigate("hospitalizacion") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Hospitalización")
                    }
                }
            }
        }
    )
}