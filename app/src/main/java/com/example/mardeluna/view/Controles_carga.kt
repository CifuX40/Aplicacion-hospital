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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import coil.compose.*
import com.google.firebase.ktx.*
import com.google.firebase.storage.ktx.*

@Composable
fun ControlesCargaScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }

    // Cargar fondo desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = Firebase.storage
        val backgroundRef =
            storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception ->
                Log.e(
                    "Firebase",
                    "Error al cargar fondo: ${exception.message}"
                )
            }
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

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Controles Carga de Autoclaves",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )

            // Descripción general
            Text(
                text = "El control de la carga es un proceso por el cual una carga es monitorizada y entregada en base al resultado de un control químico interno y un control biológico.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Sección: Integrador
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                        append("Integrador\n")
                    }
                    append("Para el control de la carga se utilizan indicadores químicos de Clase 5. Los integradores ")
                    append(" recogen las variables críticas del ciclo de esterilización y su respuesta está diseñada para imitar la de un indicador biológico.\n")
                    append("Cada control lleva impreso la carta de colores resultantes para facilitar la interpretación (insuficiente, correcto y óptimo). Se introducirá uno en cada ciclo, en doble bolsa de papel mixto, en el lugar más desfavorable de la cámara (cerca del desagüe) como si fuera un paquete aparte. Cuando acabe el ciclo y se compruebe que ha virado correctamente, se grapará a la hoja de control correspondiente.")
                },
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Sección: Control biológico
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                        append("Control biológico\n")
                    }
                    append("Son dispositivos inoculados con esporas de los microorganismos más resistentes a la esterilización por vapor. Es el único control que nos asegura la destrucción total de los microorganismos.\n")
                    append("Los controles biológicos")
                    append(" se deben utilizar una vez al día en cada autoclave de vapor, junto a la primera carga que se esterilice ese día (los días que el autoclave no se utilice no se realizará este control).")
                },
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}