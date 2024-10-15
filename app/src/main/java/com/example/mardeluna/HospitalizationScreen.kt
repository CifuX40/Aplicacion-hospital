package com.example.mardeluna

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale

@Composable
fun HospitalizationScreen(navController: NavHostController) {
    // Variable para ajustar el tamaño del círculo amarillo
    val circleSize = remember { mutableStateOf(100.dp) }

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

        // Superposición de la imagen de la habitación y el círculo amarillo
        Box(
            modifier = Modifier
                .size(300.dp), // Tamaño del contenedor para las imágenes
            contentAlignment = Alignment.Center // Alinea el círculo en el centro
        ) {
            // Imagen de la habitación
            Image(
                painter = painterResource(id = R.drawable.habitacion),
                contentDescription = "Room",
                modifier = Modifier.fillMaxSize()
            )

            // Imagen del círculo amarillo
            Image(
                painter = painterResource(id = R.drawable.circulo_amarillo),
                contentDescription = "Círculo Amarillo",
                modifier = Modifier
                    .size(circleSize.value) // Tamaño del círculo ajustable
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto descriptivo
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

        // Botones para cambiar de pantalla y ajustar el tamaño del círculo amarillo
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

            Button(
                onClick = {
                    // Ajustar el tamaño del círculo amarillo
                    circleSize.value = if (circleSize.value == 100.dp) 150.dp else 100.dp
                }
            ) {
                Text(text = "Cambiar tamaño círculo")
            }
        }
    }
}
