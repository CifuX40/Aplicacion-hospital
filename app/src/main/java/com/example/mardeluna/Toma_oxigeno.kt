package com.example.mardeluna

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.FirebaseStorage

@Composable
fun TomaOxigenoScreen(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar imagen desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("toma_oxigeno.jpg")

        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "Imagen cargada exitosamente: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen: ${exception.message}")
            }
    }

    // Contenido de la pantalla
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
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Imagen
        when {
            imageUrl.isNotEmpty() && !loadError -> {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
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
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        // Procedimiento
        Text(
            text = "PROCEDIMIENTO:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
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
            lineHeight = 24.sp
        )
    }
}