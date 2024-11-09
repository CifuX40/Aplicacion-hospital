package com.example.mardeluna

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import coil.compose.rememberAsyncImagePainter

@Composable
fun PatientAspirationScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen de aspiración de paciente desde Firebase Storage
        Image(
            painter = rememberAsyncImagePainter("gs://mar-de-luna-ada79.firebasestorage.app/aspiracion_paciente.jpg"),
            contentDescription = "Aspiración para paciente",
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para navegar a la pantalla de hospitalización
        Button(onClick = { navController.navigate("hospitalization_screen") }) {
            Text(text = "Volver a Hospitalización")
        }
    }
}