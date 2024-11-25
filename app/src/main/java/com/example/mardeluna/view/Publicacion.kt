package com.example.mardeluna.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.FirebaseStorage

// Pantalla principal con fondo y publicaciones
@Composable
fun AgregarPublicacionUI(param: (Any, Any) -> Unit) {
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Descargar fondo desde Firebase
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundUrl = uri.toString()
                Log.d("Firebase", "Fondo cargado exitosamente: $backgroundUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar el fondo: ${exception.message}")
            }
    }

    // Contenedor principal con fondo y contenido
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Mostrar fondo
        if (backgroundUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Fondo alternativo en caso de error
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PublicacionesContent()
        }
    }
}

// Contenido de la pantalla de publicaciones
@Composable
private fun PublicacionesContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Título
        Text(
            text = "Publicaciones",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Información de publicaciones
        Text(
            text = """
                Bienvenido a la sección de publicaciones.
                Aquí podrás ver las publicaciones compartidas por otros usuarios.
                ¡Explora y comparte!
            """.trimIndent(),
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ejemplo de publicación
        PublicacionExample()
    }
}

// Ejemplo de visualización de publicación
@Composable
fun PublicacionExample() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Nombre del usuario
        Text(
            text = "Usuario Ejemplo",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Contenido del texto
        Text(
            text = "Esta es una publicación de ejemplo para demostrar el diseño.",
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Imagen de ejemplo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Imagen aquí",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
