package com.example.mardeluna

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter

@Composable
fun AspiracionScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen de aspiración del paciente desde Firebase Storage
        val imageUrl = "gs://mar-de-luna-ada79.firebasestorage.app/aspiracion_paciente.jpg"
        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = "Aspiración del paciente",
            modifier = Modifier
                .size(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
