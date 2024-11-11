package com.example.mardeluna

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*

@Composable
fun ICUScreen(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("sala_uci_uno.jpg")

        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "Imagen cargada exitosamente: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen: ${exception.message}")
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Unidad de cuidados intensivos",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = Color(0xFF0D47A1) // Azul oscuro
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!loadError && imageUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Sala UCI",
                modifier = Modifier.size(300.dp)
            )
        } else {
            Text(text = "Error al cargar la imagen", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sala para cuidados de pacientes después de intervenciones quirúrgicas " +
                    "que necesitan monitorización y vigilancia, ya sea por control del dolor o " +
                    "por control de constantes vitales.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d("Navigation", "Navegando a UCI Postquirúrgica")
                navController.navigate("uci_postquirurgica")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(text = "UCI Postquirúrgica")
        }

        Button(
            onClick = {
                Log.d("Navigation", "Navegando a UCI Médica")
                navController.navigate("uci_medica")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(text = "UCI Médica")
        }
    }
}