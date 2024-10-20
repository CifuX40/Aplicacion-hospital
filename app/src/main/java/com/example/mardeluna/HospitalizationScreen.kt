package com.example.mardeluna

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@Composable
fun HospitalizationScreen(navController: NavHostController) {
    // El tamaño del círculo amarillo es fijo y no se puede cambiar
    val fixedCircleSize = 10.dp // Hacemos el círculo más pequeño y fijo

    // Variables para definir la posición del círculo amarillo
    val xOffset = (120).dp // Desplazamiento en el eje X
    val yOffset = (100).dp // Desplazamiento en el eje Y

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally // Alinea todos los elementos al centro horizontalmente
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
            contentAlignment = Alignment.TopStart // Define el punto de origen para la posición manual
        ) {
            // Imagen de la habitación
            Image(
                painter = painterResource(id = R.drawable.habitacion),
                contentDescription = "Room",
                modifier = Modifier.fillMaxSize() // La imagen ocupa todo el tamaño del contenedor
            )

            // Imagen del círculo amarillo con tamaño fijo y posición manual
            Image(
                painter = painterResource(id = R.drawable.circulo_amarillo),
                contentDescription = "Círculo Amarillo",
                modifier = Modifier
                    .size(fixedCircleSize) // Tamaño fijo del círculo amarillo
                    .offset(x = xOffset, y = yOffset) // Ajustamos la posición del círculo en los ejes X e Y
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

        // Botones para cambiar de pantalla
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Alinea los botones uniformemente
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
