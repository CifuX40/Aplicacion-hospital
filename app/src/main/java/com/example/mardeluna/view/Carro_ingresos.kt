package com.example.mardeluna.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.FirebaseStorage
import androidx.navigation.NavHostController
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.layout.ContentScale

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
                Log.e(
                    "Firebase",
                    "Error al obtener URL de la imagen del carro: ${exception.message}"
                )
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

            // Mostrar imagen del carro si está disponible
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

            // Texto explicativo
            Text(
                text = "CARRO INGRESO EN UCI",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Descripción adicional
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