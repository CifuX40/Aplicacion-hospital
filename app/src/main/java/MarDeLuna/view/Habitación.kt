package marDeLuna.view

import android.util.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Habitacion(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri ->
                backgroundUrl = uri.toString()
                Log.d("Firebase", "Fondo cargado exitosamente: $backgroundUrl")
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error al cargar el fondo: ${exception.message}")
            }

        val imageRef = storage.reference.child("habitacion.jpg")
        imageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "Imagen cargada exitosamente: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen: ${exception.message}")
            }
    }
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
                            .background(Color.LightGray)
                    )
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Habitación",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        textAlign = TextAlign.Center
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(300.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            if (imageUrl.isNotEmpty() && !loadError) {
                                Image(
                                    painter = rememberAsyncImagePainter(imageUrl),
                                    contentDescription = "Imagen de la habitación",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else if (loadError) {
                                Text(
                                    text = "Error al cargar la imagen",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            } else {
                                Text(
                                    text = "Cargando imagen...",
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Todas las habitaciones tienen una aspiración y una toma de oxígeno " +
                                    "que se deberán comprobar su funcionamiento después de cada alta de paciente " +
                                    "y limpieza de las mismas.",
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Aparataje",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = { navController.navigate("aspiracion") }) {
                                Text(text = "Aspiración")
                            }

                            Button(onClick = { navController.navigate("toma_oxigeno") }) {
                                Text(text = "Toma de oxígeno")
                            }
                        }
                    }
                }
            }
        }
    )
}