package com.example.mardeluna.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.navigation.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Evita300Screen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Evita 300") }
            )
        },
        content = { paddingValues ->
            // Usa el padding de la pantalla correctamente
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text(
                    text = "Evita 300",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    )
}
