package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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

@Composable
fun CarroIngresosScreen(navController: NavHostController) {
    var backgroundImageUrl by remember { mutableStateOf<String?>(null) }
    var carroImageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Descargar la URL de la imagen de fondo y la del carro de ingresos
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo
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

        // Cargar imagen del carro de ingresos
        val carroRef = storage.reference.child("carro_ingresos.jpg")
        carroRef.downloadUrl
            .addOnSuccessListener { uri ->
                carroImageUrl = uri.toString()
                Log.d("Firebase", "URL de imagen del carro obtenida: $carroImageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al obtener URL de la imagen del carro: ${exception.message}")
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

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top, // Alineación de los elementos hacia la parte superior
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Carro de ingresos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Texto explicativo
            Text(
                text = "CARRO INGRESO EN UCI",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            // Mostrar imagen del carro si está disponible
            carroImageUrl?.let {
                Image(
                    painter = rememberAsyncImagePainter(model = it),
                    contentDescription = "Carro de ingresos",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .padding(bottom = 10.dp)
                )
            } ?: run {
                Text(
                    text = "Cargando imagen del carro...",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}