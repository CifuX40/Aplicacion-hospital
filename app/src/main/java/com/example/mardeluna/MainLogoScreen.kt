package com.example.mardeluna

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun MainLogoScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.piso_1_logo),
            contentDescription = "Logo de la Primera Planta",
            modifier = Modifier
                .size(200.dp)
                .clickable { navController.navigate("first_floor") }
                .padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.piso_2_logo),
            contentDescription = "Logo de la Segunda Planta",
            modifier = Modifier
                .size(200.dp)
                .clickable { navController.navigate("second_floor") }
                .padding(bottom = 16.dp)
        )
    }
}