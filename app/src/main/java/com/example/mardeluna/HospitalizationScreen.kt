package com.example.mardeluna

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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

        Image(
            painter = painterResource(id = R.drawable.habitacion),
            contentDescription = "Room",
            modifier = Modifier
                .size(300.dp)
                .clickable { navController.navigate("room_screen") }
        )

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

        // Botones de Aspiración y Toma de oxígeno
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
