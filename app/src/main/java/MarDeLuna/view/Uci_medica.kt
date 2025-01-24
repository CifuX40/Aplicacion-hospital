package marDeLuna.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
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
fun UciMedica(navController: NavHostController) {
    var backgroundImageUrl by remember { mutableStateOf<String?>(null) }
    var salaImageUrl by remember { mutableStateOf<String?>(null) }
    var criteriosImageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val fondoRef = storage.reference.child("fondo_de_pantalla.jpg")
        fondoRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundImageUrl = uri.toString() }
            .addOnFailureListener { loadError = true }
        val salaRef = storage.reference.child("sala_uci_uno.jpg")
        salaRef.downloadUrl
            .addOnSuccessListener { uri -> salaImageUrl = uri.toString() }
            .addOnFailureListener { loadError = true }
        val criteriosRef = storage.reference.child("criterios_admision_uci.jpg")
        criteriosRef.downloadUrl
            .addOnSuccessListener { uri -> criteriosImageUrl = uri.toString() }
            .addOnFailureListener { loadError = true }
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (backgroundImageUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = backgroundImageUrl),
                    contentDescription = "Fondo de pantalla",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else if (loadError) {
                Text(
                    text = "Error al cargar el fondo",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Text(
                    text = "Cargando fondo...",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "UCI Médica",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                salaImageUrl?.let {
                    Image(
                        painter = rememberAsyncImagePainter(model = it),
                        contentDescription = "Sala de UCI",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(bottom = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = "Criterios de admisión en UCI Médica",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "La definición dada a la UCI delimita los dos criterios clave para los pacientes en la Unidad:\n" +
                            "- Que precisen un elevado nivel de cuidados.\n" +
                            "- Que sean recuperables.",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                criteriosImageUrl?.let {
                    Image(
                        painter = rememberAsyncImagePainter(model = it),
                        contentDescription = "Criterios de admisión en UCI",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { navController.navigate("desfibrilador") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Desfibrilador")
                    }

                    Button(
                        onClick = { navController.navigate("evita_600") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Evita 600")
                    }
                }
            }
        }
    }
}