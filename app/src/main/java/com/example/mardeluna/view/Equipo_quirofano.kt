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
import androidx.compose.ui.layout.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.ktx.*
import com.google.firebase.storage.ktx.*

@Composable
fun EquipoQuirofanoScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }

    // Cargar fondo desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = Firebase.storage
        val backgroundRef =
            storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error al cargar fondo: ${exception.message}")
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
            horizontalAlignment = Alignment.Start
        ) {
            // Título
            Text(
                text = "Equipos de quirófano",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Texto de roles
            RoleSection(
                title = "Supervisor de Qx",
                points = listOf(
                    "Gestionar la reserva de salas de Qx.",
                    "Coordinar con los cirujanos la cirugía de urgencia.",
                    "Confirmar con otros servicios implicados en la cirugía que están avisados y tienen prevista su intervención.",
                    "Velar por el buen funcionamiento de todos los procesos relacionados con la gestión del bloque quirúrgico (esterilización, instrumental, caducidades, limpieza, controles ambientales, mantenimiento de instalaciones, controles desfibrilador, etc.).",
                    "Velar por el adecuado cumplimiento del Protocolo de trasplante de Tejidos de origen humano."
                )
            )

            RoleSection(
                title = "Cirujano",
                points = listOf(
                    "Planificar la cirugía programada con el Supervisor de Qx y Admisión.",
                    "Informar al paciente del proceso a realizar, entregarle el consentimiento informado específico para la intervención planificada y los volantes necesarios.",
                    "Coordinación con otros servicios (laboratorio, anatomía patológica, etc.).",
                    "Cumplimentación del consentimiento informado.",
                    "Aplicar la normativa especificada en los protocolos de Quirófano (lavado de manos, accesos, vestimenta...).",
                    "Realizar las comprobaciones indicadas en el Listado de Verificación y registrarlas.",
                    "Cumplimentar los registros específicos en caso de implante de células o tejidos de origen humano o sus derivados."
                )
            )

            RoleSection(
                title = "Anestesista",
                points = listOf(
                    "Atender inicialmente al paciente en nombre del equipo médico.",
                    "Cumplimentación de consentimiento informado.",
                    "Revisar el equipo de anestesia.",
                    "Aplicar la normativa especificada en los protocolos de Quirófano (lavado de manos, accesos, vestimenta...).",
                    "Realizar las comprobaciones indicadas en el Listado de Verificación de Qx y registrarlas.",
                    "Controlar el estado postoperatorio del paciente hasta que él mismo autorice el alta en Qx."
                )
            )

            RoleSection(
                title = "Circulante quirófano",
                points = listOf(
                    "Comprobar el estado de la sala antes de la intervención y la disponibilidad del material necesario.",
                    "Aplicar la normativa especificada en los protocolos de Quirófano (lavado de manos, accesos, vestimenta...).",
                    "Realizar las comprobaciones indicadas en el Listado de Verificación y registrarlas.",
                    "Controlar todo el material fungible que se utilice en cada una de las intervenciones, verificando la fecha de caducidad.",
                    "Registrar la información asociada a la intervención (horas, equipo, material utilizado, hoja de implantes, contaje de gasas/compresas, injertos, identificación de muestras, etc.)."
                )
            )

            RoleSection(
                title = "Celadores",
                points = listOf(
                    "Trasladar al paciente al bloque quirúrgico.",
                    "Confirmar la identidad del paciente y la cirugía programada.",
                    "Confirmar la realización del preoperatorio."
                )
            )

            RoleSection(
                title = "Enfermera REA",
                points = listOf(
                    "Confirmar la identidad de los pacientes que acceden al bloque quirúrgico y la cirugía programada.",
                    "Confirmar la realización del preoperatorio.",
                    "Registrar los cuidados y medicación que se administren al paciente en el despertar.",
                    "Realizar las comprobaciones indicadas en el Listado de Verificación y registrarlas antes del abandono del bloque quirúrgico."
                )
            )
        }
    }
}

@Composable
fun RoleSection(title: String, points: List<String>) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color.Black,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    points.forEach { point ->
        Text(
            text = "• $point",
            fontSize = 16.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )
    }
}