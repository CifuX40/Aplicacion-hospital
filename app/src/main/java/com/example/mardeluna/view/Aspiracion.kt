package com.example.mardeluna.view

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Aspiracion(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar imagenes desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar el fondo de pantalla
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundUrl = uri.toString()
                Log.d("Firebase", "Fondo cargado exitosamente: $backgroundUrl")
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error al cargar el fondo: ${exception.message}")
            }

        // Cargar la imagen de aspiración
        val storageRef = storage.reference.child("aspiracion_paciente.jpg")
        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "Imagen de aspiración cargada exitosamente: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen de aspiración: ${exception.message}")
            }
    }

    // Scaffold con TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mar de Luna",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Plantas") }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Fondo de pantalla
                if (backgroundUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(backgroundUrl),
                        contentDescription = "Fondo de pantalla",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray)
                    )
                }

                // Contenido principal
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aspiración",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = Color.Black
                    )

                    // Imagen de aspiración
                    when {
                        imageUrl.isNotEmpty() && !loadError -> {
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = "Imagen de aspiración",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .padding(bottom = 16.dp)
                            )
                        }

                        loadError -> {
                            Text(
                                text = "Error al cargar la imagen.",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        else -> {
                            Text(
                                text = "Cargando imagen...",
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }

                    // Procedimiento
                    Text(
                        text = "PROCEDIMIENTO:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = Color.Black
                    )
                    Text(
                        text = """
1. Verificar el funcionamiento del aspirador conectándolo a la toma de vacío, asegurando una presión negativa adecuada (80 a 120 mmHg).
2. Limpiar externamente las fosas nasales del paciente si es necesario.
3. Abrir la sonda de aspiración y conectar el tubo a la pieza en "Y".
4. Introducir suavemente la sonda en una fosa nasal, evitando la succión durante la introducción para proteger la mucosa nasal. 
   - Una vez en la posición adecuada, iniciar la aspiración con movimientos rotatorios mientras se retira la sonda.
   - Tapar intermitentemente el orificio de la conexión en "Y" para controlar la succión.
5. Repetir el procedimiento en la otra fosa nasal utilizando una sonda nueva.
6. Para aspirar la cavidad orofaríngea, repetir el proceso descrito anteriormente.
7. Si se requiere acceder a los bronquios:
   - Colocar la cabeza del paciente en hiperextensión.
   - Girar la cabeza hacia el lado opuesto al bronquio que se desea aspirar.
8. Si el objetivo es recolectar una muestra, utilizar una sonda con reservorio.
9. Al finalizar:
   - Lavar la sonda y el tubo aspirador con agua destilada o suero fisiológico.
   - Desechar el material en los contenedores indicados.
   - Retirar los guantes y realizar higiene de manos.
                    """.trimIndent(),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color.Black
                    )
                }
            }
        }
    )
}