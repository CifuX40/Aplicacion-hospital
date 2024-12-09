package com.example.mardeluna.view

import android.content.*
import android.net.*
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
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.google.firebase.storage.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaginasWeb(navController: NavHostController) {
    var backgroundImageUrl by remember { mutableStateOf<String?>(null) }
    var webPageImageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }
    var secondLoadError by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Información", "Páginas Web")

    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
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

    Scaffold(
        topBar = {
            Column {
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

                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (backgroundImageUrl != null && !loadError) {
                Image(
                    painter = rememberAsyncImagePainter(backgroundImageUrl),
                    contentDescription = "Fondo de pantalla",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (loadError) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                )
            }

            when (selectedTabIndex) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Páginas web",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        when {
                            webPageImageUrl != null && !secondLoadError -> {
                                Image(
                                    painter = rememberAsyncImagePainter(webPageImageUrl),
                                    contentDescription = "Imagen de páginas web",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)
                                        .padding(bottom = 16.dp)
                                )

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
                                    text = "Error al cargar la imagen.",
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

                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Links para ir a las diferentes paginas",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Button(
                            onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://www.aturnos.com/?gad_source=1&gclid=Cj0KCQiA3sq6BhD2ARIsAJ8MRwXZR34QAw4n8zVEZcTcJmqQJ-yS-QGKBnwfDAey41QQmykOfeE5t-IaApdsEALw_wcB")
                                )
                                navController.context.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(text = "aTurnos")
                        }

                        Button(
                            onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://gesdie.hmhospitales.com/gesdie/helloentrada.htm")
                                )
                                navController.context.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(text = "Gesdie")
                        }

                        Button(
                            onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://www.portalempleado.net/indexc.html")
                                )
                                navController.context.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(text = "Portal del Empleado")
                        }
                    }
                }
            }
        }
    }
}