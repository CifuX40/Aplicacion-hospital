package com.example.mardeluna.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mardeluna.R

@Composable
fun MainLogoScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen para la primera planta como botón
        Image(
            painter = painterResource(id = R.drawable.piso_1_logo),
            contentDescription = "Logo de la Primera Planta",
            modifier = Modifier
                .size(200.dp)
                .clickable { navController.navigate("first_floor") }  // Navegar a la primera planta al hacer clic
                .padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen para la segunda planta como botón
        Image(
            painter = painterResource(id = R.drawable.piso_2_logo),
            contentDescription = "Logo de la Segunda Planta",
            modifier = Modifier
                .size(200.dp)
                .clickable { navController.navigate("second_floor") }  // Navegar a la segunda planta al hacer clic
                .padding(bottom = 16.dp)
        )
    }
}
