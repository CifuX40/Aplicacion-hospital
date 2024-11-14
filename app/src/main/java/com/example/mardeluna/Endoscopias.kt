package com.example.mardeluna

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.storage.FirebaseStorage

@Composable
fun Endoscopias(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar la URL de la imagen desde Firebase Storage
    LaunchedEffect(Unit) {
        loadImageFromFirebase { url, error ->
            imageUrl = url ?: ""
            loadError = error != null
            error?.let { Log.e("Firebase", "Error al cargar la imagen: ${it.message}") }
        }
    }

    // Configuración de desplazamiento vertical y contenido
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Content(navController, loadError, imageUrl)
    }
}

// Encapsulando la lógica de carga de imagen
private fun loadImageFromFirebase(onResult: (String?, Exception?) -> Unit) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child("endoscopias_camilla.jpg")
    storageRef.downloadUrl
        .addOnSuccessListener { uri -> onResult(uri.toString(), null) }
        .addOnFailureListener { exception -> onResult(null, exception) }
}

@Composable
private fun Content(navController: NavHostController, loadError: Boolean, imageUrl: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Sala de Endoscopias",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar imagen si se carga correctamente
        if (!loadError && imageUrl.isNotEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Sala de endoscopias",
                modifier = Modifier
                    .size(300.dp)
                    .padding(8.dp)
            )
        } else if (loadError) {
            Text(
                text = "Error al cargar la imagen",
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto adicional sobre la sala y el procedimiento
        Text(
            text = """
                En la sala de endoscopias encontramos:
                - Torre de endoscopias: con una pantalla que visualizará todo el procedimiento.
                - Camilla: donde se tumba al paciente.
                - Zona de anestesia: Consta de un carro de medicación y una zona de gases anestésicos.
                
                Procedimiento:
                Pasamos al paciente y se le retira toda la ropa, el anestesista le canaliza una vía periférica por donde administrará la medicación para su sedación.
                
                La enfermera de endoscopias deberá tener preparado el siguiente material:
                - Torre montada con aspiración e irrigación.
                - Endoscopio colocado en la torre y comprobado el funcionamiento.
                - Medicación del anestesista preparada en bandeja.
                - Control de posibles anomalías patológicas.
            """.trimIndent(),
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de navegación
        NavigationButtons(navController)
    }
}

@Composable
private fun NavigationButtons(navController: NavHostController) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(onClick = { navController.navigate("endoscopios_screen") }) {
            Text("Endoscopios")
        }

        Button(onClick = { navController.navigate("lavadora_screen") }) {
            Text("Lavadora")
        }
    }
}