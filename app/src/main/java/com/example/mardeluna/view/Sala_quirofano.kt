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
fun SalaQuirofano(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var salaQuirofanoImageUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val storage = Firebase.storage
        val backgroundRef =
            storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error al cargar fondo: ${exception.message}")
            }
        val salaQuirofanoRef =
            storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/sala_quirofano.jpg")
        salaQuirofanoRef.downloadUrl
            .addOnSuccessListener { uri -> salaQuirofanoImageUrl = uri.toString() }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error al cargar imagen de sala quirófano: ${exception.message}")
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Sala quirófano",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (salaQuirofanoImageUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(salaQuirofanoImageUrl),
                        contentDescription = "Sala quirófano",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(bottom = 16.dp)
                    )
                }

                Text(
                    text = "Es el lugar donde se realizan las intervenciones quirúrgicas. " +
                            "Deberá proporcionar un ambiente seguro, eficiente y aséptico para la realización de procedimientos quirúrgicos.",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = { navController.navigate("equipo_quirofano") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Equipos de quirófano")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("chequeo_respirador") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Chequeo respirador")
                }
            }
        }
    }
}