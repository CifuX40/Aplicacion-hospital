package com.example.mardeluna

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.rememberAsyncImagePainter

@Composable
fun HospitalizationScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Habitaciones de hospitalización.",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(300.dp),
            contentAlignment = Alignment.TopStart
        ) {
            // Imagen de la habitación desde Firebase Storage
            Image(
                painter = rememberAsyncImagePainter("gs://mar-de-luna-ada79.firebasestorage.app/habitacion.jpg"),
                contentDescription = "Room",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Todas las habitaciones tienen una aspiración y una toma de oxígeno " +
                    "que se deberán comprobar su funcionamiento después de cada alta de paciente " +
                    "y limpieza de las mismas.",
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Aparataje",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.navigate("aspiracion_screen") }
            ) {
                Text(text = "Aspiración")
            }

            Button(
                onClick = { navController.navigate("toma_oxigeno_screen") }
            ) {
                Text(text = "Toma de oxígeno")
            }
        }
    }
}