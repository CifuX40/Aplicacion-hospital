package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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

@Composable
fun EsterilizacionScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var sterilizationImageUrl by remember { mutableStateOf("") }

    // Cargar imágenes desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = Firebase.storage

        // Fondo
        val backgroundRef = storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception -> Log.e("Firebase", "Error al cargar fondo: ${exception.message}") }

        // Imagen de esterilización
        val sterilizationRef = storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/esterilizacion.jpg")
        sterilizationRef.downloadUrl
            .addOnSuccessListener { uri -> sterilizationImageUrl = uri.toString() }
            .addOnFailureListener { exception -> Log.e("Firebase", "Error al cargar imagen esterilización: ${exception.message}") }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla
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
            // Título
            Text(
                text = "Esterilización",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Imagen de esterilización
            if (sterilizationImageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(sterilizationImageUrl),
                    contentDescription = "Imagen de esterilización",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(bottom = 16.dp)
                )
            }

            // Texto informativo
            Text(
                text = "Dependiendo de las características del material y su uso serán necesarias medidas distintas para su tratamiento. " +
                        "El material clínico puede dividirse en tres categorías en función del riesgo que se deriva de su uso: no crítico, semicrítico y, crítico.\n\n" +
                        "Material no crítico\n" +
                        "Todo aquel material que entra en contacto con piel intacta pero nunca con membranas mucosas. Requiere un proceso de limpieza, seguido de una desinfección de bajo nivel.\n\n" +
                        "Material semicrítico\n" +
                        "Requiere un proceso de limpieza, seguido de una desinfección de alto nivel. Todo aquel material que entra en contacto con membranas mucosas o piel no intacta.\n\n" +
                        "Material crítico\n" +
                        "Todo aquel material que rompe la barrera cutáneo-mucosa y penetra en cavidad estéril.\n" +
                        "Requiere un proceso de limpieza, seguido de esterilización.\n\n" +
                        "Desinfección de bajo-intermedio nivel\n" +
                        "Empleo de un procedimiento químico con el que se pueden destruir formas vegetativas bacterianas, algunos virus y hongos.\n\n" +
                        "Desinfección de alto nivel\n" +
                        "Empleo de un procedimiento químico con el que se consigue destruir la mayor parte de los microorganismos, excepto esporas bacterianas.\n\n" +
                        "Esterilización\n" +
                        "Empleo de un procedimiento fisicoquímico dirigido a destruir toda la flora microbiana, incluidas las esporas bacterianas, altamente resistentes. Se realizará en las maquinas esterilizadoras o Autoclaves.",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Botones
            Button(
                onClick = { navController.navigate("empaquetado_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Empaquetado")
            }

            Button(
                onClick = { navController.navigate("controles_carga_autoclaves_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Controles carga de autoclaves")
            }
        }
    }
}