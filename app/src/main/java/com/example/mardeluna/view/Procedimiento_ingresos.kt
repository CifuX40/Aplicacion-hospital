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
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import android.util.*
import coil.compose.*
import com.google.firebase.storage.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcedimientoIngresos(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error al cargar fondo: ${exception.message}")
            }

        // Cargar la imagen del procedimiento
        val imageRef = storage.reference.child("procedimiento_ingresos.jpg")
        imageRef.downloadUrl
            .addOnSuccessListener { uri -> imageUrl = uri.toString() }
            .addOnFailureListener { exception ->
                Log.e(
                    "Firebase",
                    "Error al cargar la imagen del procedimiento: ${exception.message}"
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
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
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
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Procedimiento ingresos",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mostrar la imagen del procedimiento, si está disponible
                    if (imageUrl.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = "Procedimiento ingresos",
                            modifier = Modifier
                                .height(500.dp)
                                .width(500.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Text(
                            text = "Error al cargar la imagen del procedimiento",
                            color = Color.Red
                        )
                    }
                }
            }
        }
    )
}