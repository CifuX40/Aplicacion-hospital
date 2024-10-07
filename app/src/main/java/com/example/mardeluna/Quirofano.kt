package com.example.mardeluna

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun SurgeryScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Quirófano", fontWeight = FontWeight.Bold,
            fontSize = 18.sp)

        Image(
            painter = painterResource(id = R.drawable.rea),
            contentDescription = "Rea",
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp)
        )
        Text(text = "Área de recuperación", modifier = Modifier.padding(8.dp))

        Image(
            painter = painterResource(id = R.drawable.sala_quirofano),
            contentDescription = "Sala de quirófano",
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp)
        )
        Text(text = "Sala de quirófano", modifier = Modifier.padding(8.dp))

        Image(
            painter = painterResource(id = R.drawable.esterilizacion),
            contentDescription = "Esterilización",
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp)
        )
        Text(text = "Área de esterilización", modifier = Modifier.padding(8.dp))
    }
}
