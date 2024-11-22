package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*

@Composable
fun PaginasWebScreen(navController: NavHostController) {
    // Variables de estado para las URLs de las imágenes
    var backgroundImageUrl by remember { mutableStateOf<String?>(null) }
    var webPageImageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }
    var secondLoadError by remember { mutableStateOf(false) }

    // Cargar imágenes desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo de pantalla
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundImageUrl = uri.toString()
                Log.d("Firebase", "Fondo cargado exitosamente: $backgroundImageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar el fondo: ${exception.message}")
            }

        // Cargar imagen de "Páginas web"
        val webPageRef = storage.reference.child("paginas_web.jpg")
        webPageRef.downloadUrl
            .addOnSuccessListener { uri ->
                webPageImageUrl = uri.toString()
                Log.d("Firebase", "Imagen de Páginas web cargada exitosamente: $webPageImageUrl")
            }
            .addOnFailureListener { exception ->
                secondLoadError = true
                Log.e("Firebase", "Error al cargar la imagen de Páginas web: ${exception.message}")
            }
    }

    // Contenedor principal con fondo
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Imagen de fondo que ocupa toda la pantalla
        if (backgroundImageUrl != null && !loadError) {
            Image(
                painter = rememberAsyncImagePainter(backgroundImageUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (loadError) {
            // Fondo alternativo en caso de error al cargar la imagen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
        }

        // Contenido superpuesto al fondo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título principal "Páginas web" en negrita y color negro
            Text(
                text = "Páginas web",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Imagen de "Páginas web" desde Firebase Storage
            when {
                webPageImageUrl != null && !secondLoadError -> {
                    Image(
                        painter = rememberAsyncImagePainter(webPageImageUrl),
                        contentDescription = "Imagen de páginas web",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(bottom = 16.dp) // Ajusta el espacio inferior
                    )

                    // Texto informativo debajo de la imagen
                    Text(
                        text = """
                Las páginas web que debes conocer son las siguientes:
                aTurnos: Encontrarás tus planillas. Desde aquí solicitarás tus cambios, vacaciones, días libres, etc.
                Correo Web: Tu correo corporativo donde recibirás toda tu información de diferentes departamentos, cursos y comunicaciones internas.
                GersDie: Es la plataforma donde meterás tus doblajes que serán aprobados por el supervisor.
                Portal del Empleado/PeoPlenet: Desde aquí puedes consultar todas tus nóminas, modificar tus datos personales y bancarios, también encontrarás información sobre los beneficios sociales que te ofrece el hospital.
            """.trimIndent(),
                        fontSize = 14.sp,
                        color = Color.Black,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                    )
                }

                secondLoadError -> {
                    Text(
                        text = "Error al cargar la imagen de Páginas web",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                else -> {
                    Text(
                        text = "Cargando imagen de Páginas web...",
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }
        }
    }
}