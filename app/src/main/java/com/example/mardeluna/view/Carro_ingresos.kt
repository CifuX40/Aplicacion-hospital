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
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import coil.compose.*
import com.google.firebase.storage.*
import androidx.navigation.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarroIngresos(navController: NavHostController) {
    var backgroundImageUrl by remember { mutableStateOf<String?>(null) }
    var carroImageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundImageUrl = uri.toString()
                Log.d("Firebase", "URL de fondo obtenida: $backgroundImageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al obtener URL del fondo: ${exception.message}")
            }

        val carroRef = storage.reference.child("carro_ingresos.jpg")
        carroRef.downloadUrl
            .addOnSuccessListener { uri ->
                carroImageUrl = uri.toString()
                Log.d("Firebase", "URL de imagen del carro obtenida: $carroImageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e(
                    "Firebase",
                    "Error al obtener URL de la imagen del carro: ${exception.message}"
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Carro de ingresos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                carroImageUrl?.let {
                    Image(
                        painter = rememberAsyncImagePainter(model = it),
                        contentDescription = "Carro de ingresos",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(bottom = 16.dp)
                    )
                } ?: run {
                    Text(
                        text = "Cargando imagen del carro...",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Text(
                    text = "CARRO INGRESO EN UCI",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "El carro e ingreso de UCI contiene todo el material necesario para el ingreso de cualquier paciente, evitando pérdidas de tiempo y faltas de material en un momento tan crítico.\n" +
                            "Cada cajón lleva un cartel con el contenido que deberá haber dentro del mismo. \n" +
                            "Antes de recibir cualquier ingreso y siempre después de haber hecho uso de él deberá revisarse y reponerse todo el material utilizado.",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}