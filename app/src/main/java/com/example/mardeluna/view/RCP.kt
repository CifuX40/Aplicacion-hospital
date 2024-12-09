package com.example.mardeluna.view

import android.net.*
import android.util.*
import android.widget.*
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
import com.google.firebase.storage.*
import androidx.compose.ui.viewinterop.*
import coil.compose.*
import androidx.compose.ui.layout.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RCP(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var videoBasicUrl by remember { mutableStateOf<String?>(null) }
    var imageUrl by remember { mutableStateOf("") }
    var videoAdvancedUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        storage.reference.child("fondo_de_pantalla.jpg").downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { Log.e("Firebase", "Error al cargar fondo: ${it.message}") }
        storage.reference.child("rcp_basica.mp4").downloadUrl
            .addOnSuccessListener { uri -> videoBasicUrl = uri.toString() }
            .addOnFailureListener {
                Log.e("Firebase", "Error al cargar video básico: ${it.message}")
            }
        storage.reference.child("esquema_rcp.jpg").downloadUrl
            .addOnSuccessListener { uri -> imageUrl = uri.toString() }
            .addOnFailureListener { Log.e("Firebase", "Error al cargar esquema: ${it.message}") }
        storage.reference.child("rcp_avanzada.mp4").downloadUrl
            .addOnSuccessListener { uri -> videoAdvancedUrl = uri.toString() }
            .addOnFailureListener {
                Log.e("Firebase", "Error al cargar video avanzado: ${it.message}")
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
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "RCP",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    videoBasicUrl?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "RCP Básico",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        AndroidView(
                            factory = { context ->
                                VideoView(context).apply {
                                    setVideoURI(Uri.parse(it))
                                    val mediaController = MediaController(context)
                                    mediaController.setAnchorView(this)
                                    setMediaController(mediaController)
                                    requestFocus()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        )
                    }

                    if (imageUrl.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Esquema RCP",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = "Esquema RCP",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }

                    videoAdvancedUrl?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "RCP Avanzado",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        AndroidView(
                            factory = { context ->
                                VideoView(context).apply {
                                    setVideoURI(Uri.parse(it))
                                    val mediaController = MediaController(context)
                                    mediaController.setAnchorView(this)
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
    )
}