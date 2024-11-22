package com.example.mardeluna.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*

@Composable
fun HistoryScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }

    // Cargar la URL del fondo de pantalla desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")

        backgroundRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundUrl = uri.toString()
            }
            .addOnFailureListener {
            }
    }

    // Contenedor principal con fondo
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla
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

        // Contenido de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Historia del Hospital Mar de Luna",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = """
                    La Dirección y todo el personal que integra el Hospital Mar de Luna le damos una cálida bienvenida a su nuevo puesto.
                    
                    Para Hospital Mar de Luna, usted es de suma importancia y, por esta razón, queremos facilitarle sus primeros días con nosotros. Hemos preparado una guía didáctica que le explicará el funcionamiento del mismo.

                    Nuestro propósito es hacer que sus primeros días sean más sencillos, facilitar su proceso de adaptación y hacer más cómoda su rutina diaria.
                    
                    Deseamos que su permanencia en nuestro centro le permita crecer profesionalmente y nos ayude a mejorar la calidad de la atención que brindamos a nuestros pacientes.
                    
                    Nuestro lema es siempre poner al paciente en el centro del cuidado y sabemos que esto no es posible sin cuidar al profesional. Por ello, consideramos esta guía como el inicio de una relación que esperamos sea beneficiosa para ambas partes.

                    Historia, presente pasado y futuro:
                    En 2004 comenzamos nuestras actividades y, desde entonces, nuestra dedicación al cuidado de la salud de los pacientes y sus familias nos ha permitido convertirnos en el hospital de referencia en la zona de Mestalla, Valencia. Nuestro centro opera de manera que garantiza que nuestros pacientes reciban la más alta calidad en atención y procesos asistenciales.

                    Pilares básicos:
                    · Razón de ser, el paciente.
                    · Colaboración con el profesional.
                    · Promoción interna y retención del talento.
                    · Sistema de gestión basado en la calidad.
                    · Permanente actualización tecnológica.
                    · Búsqueda de una oferta médica integral.
                    · Apuesta por la complejidad o Equilibrio Empresa y Hospital.

                    Instalaciones Mar de Luna:
                    · 20 Consultas.
                    · 70 camas.
                    · 6 Quirófanos.
                    · Diagnóstico por imagen.

                    Compromiso con el Medio Ambiente:
                    Disponemos de un Programa de Mejora Ambiental, cuyos objetivos se van modificando según se consiguen. Algunos ejemplos son:
                    · Reducción del consumo de agua en los centros.
                    · Mejora de la eficiencia hídrica en el riego de jardines.
                    · Inversión en iluminación LED.
                    · Implantación del Sistema de Gestión Energética ISO 50001.
                    · Reducción de la generación de residuos biosanitarios especiales.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}