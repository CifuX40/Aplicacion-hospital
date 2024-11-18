package com.example.mardeluna

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
fun TomaOxigenoScreen(navController: NavHostController) {
    var backgroundImageUrl by remember { mutableStateOf("") }
    var contentImageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar imágenes desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo de pantalla
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundImageUrl = uri.toString()
                Log.d("Firebase", "Fondo cargado exitosamente: $backgroundImageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar el fondo: ${exception.message}")
            }

        // Cargar imagen de contenido
        val contentRef = storage.reference.child("toma_oxigeno.jpg")
        contentRef.downloadUrl
            .addOnSuccessListener { uri ->
                contentImageUrl = uri.toString()
                Log.d("Firebase", "Imagen cargada exitosamente: $contentImageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen: ${exception.message}")
            }
    }

    // Pantalla con fondo y contenido
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        if (backgroundImageUrl.isNotEmpty() && !loadError) {
            Image(
                painter = rememberAsyncImagePainter(backgroundImageUrl),
                contentDescription = "Fondo de pantalla",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Contenido superpuesto
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Toma de oxígeno",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color.Black
            )

            // Imagen de contenido
            when {
                contentImageUrl.isNotEmpty() && !loadError -> {
                    Image(
                        painter = rememberAsyncImagePainter(contentImageUrl),
                        contentDescription = "Imagen ilustrativa de toma de oxígeno",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(bottom = 16.dp)
                    )
                }

                loadError -> {
                    Text(
                        text = "Error al cargar la imagen.",
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                else -> {
                    Text(
                        text = "Cargando imagen...",
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = Color.Gray
                    )
                }
            }

            // Procedimiento
            Text(
                text = "PROCEDIMIENTO:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.Black
            )
            Text(
                text = """
1. Colocar al paciente en una posición cómoda y estable.
2. Conectar el caudalímetro a la toma de oxígeno de la pared.
3. Conectar el humidificador al caudalímetro.
4. Abrir el suministro de oxígeno para comprobar que el caudalímetro funciona correctamente y que el humidificador burbujea.
5. Ajustar el flujo y la concentración de oxígeno según la prescripción médica.
6. Conectar el extremo del circuito de oxígeno al dispositivo de administración correspondiente:
   - Cánula nasal (gafas nasales)
   - Mascarilla
   - Sonda nasal (catéter nasal)
                """.trimIndent(),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color.Black
            )
        }
    }
}