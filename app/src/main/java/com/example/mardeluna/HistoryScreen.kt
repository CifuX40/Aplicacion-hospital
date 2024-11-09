package com.example.mardeluna

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.navigation.*

@Composable
fun HistoryScreen(navController: NavHostController) {
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
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "La Dirección y todo el personal que integra el Hospital Mar de Luna le damos una cálida bienvenida a su nuevo puesto.\n" +
                    "Para Hospital Mar de Luna, usted es de suma importancia y, por esta razón, queremos facilitarle sus primeros días con nosotros. Hemos preparado una guía didáctica que le explicará el funcionamiento del mismo.\n" +
                    "Nuestro propósito es hacer que sus primeros días sean más sencillos, facilitar su proceso de adaptación y hacer más cómoda su rutina diaria.\n" +
                    "Deseamos que su permanencia en nuestro centro le permita crecer profesionalmente y nos ayude a mejorar la calidad de la atención que brindamos a nuestros pacientes.\n" +
                    "Nuestro lema es siempre poner al paciente en el centro del cuidado y sabemos que esto no es posible sin cuidar al profesional. Por ello, consideramos esta guía como el inicio de una relación que esperamos sea beneficiosa para ambas partes.\n" +
                    "\n" +
                    "Historia, presente pasado y futuro\n" +
                    "En 2004 comenzamos nuestras actividades y, desde entonces, nuestra dedicación al cuidado de la salud de los pacientes y sus familias nos ha permitido convertirnos en el hospital de referencia en la zona de Mestalla, Valencia. Nuestro centro opera de manera que garantiza que nuestros pacientes reciban la más alta calidad en atención y procesos asistenciales.\n" +
                    "\n" +
                    "Pilares básicos\n" +
                    "· Razón de ser, el paciente.\n" +
                    "· Colaboración con el profesional.\n" +
                    "· Promoción interna y retención del talento.\n" +
                    "· Sistema de gestión basado en la calidad.\n" +
                    "· Permanente actualización tecnológica.\n" +
                    "· Búsqueda de una oferta médica integral.\n" +
                    "· Apuesta por la complejidad o Equilibrio Empresa y Hospital.\n" +
                    "\n" +
                    "Instalaciones Mar de Luna\n" +
                    "· 20 Consultas.\n" +
                    "· 70 camas.\n" +
                    "· 6 Quirófanos.\n" +
                    "· Diagnóstico por imagen.\n" +
                    "\n" +
                    "Compromiso con el Medio Ambiente\n" +
                    "Disponemos de un Programa de Mejora Ambiental, cuyos objetivos se van modificando según se consiguen. Algunos ejemplos son:\n" +
                    "· Reducción del consumo de agua en los centros.\n" +
                    "· Mejora de la eficiencia hídrica en el riego de jardines.\n" +
                    "· Inversión en iluminación LED.\n" +
                    "· Implantación del Sistema de Gestión Energética ISO 50001.\n" +
                    "· Reducción de la generación de residuos biosanitarios especiales.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}