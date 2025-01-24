package marDeLuna.view

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
fun Plantas(navController: NavHostController) {
    var primeraPlantaUrl by remember { mutableStateOf("") }
    var segundaPlantaUrl by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { loadError = true }
        val firstFloorRef = storage.reference.child("piso_1_logo.png")
        firstFloorRef.downloadUrl
            .addOnSuccessListener { uri -> primeraPlantaUrl = uri.toString() }
            .addOnFailureListener { loadError = true }
        val secondFloorRef = storage.reference.child("piso_2_logo.png")
        secondFloorRef.downloadUrl
            .addOnSuccessListener { uri -> segundaPlantaUrl = uri.toString() }
            .addOnFailureListener { loadError = true }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (backgroundUrl.isNotEmpty() && !loadError) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (loadError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!loadError) {
                Text(
                    text = "Plantas",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                if (primeraPlantaUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(primeraPlantaUrl),
                        contentDescription = "Logo de la Primera Planta",
                        modifier = Modifier
                            .size(200.dp)
                            .clickable { navController.navigate("primera_planta") }
                            .padding(bottom = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (segundaPlantaUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(segundaPlantaUrl),
                        contentDescription = "Logo de la Segunda Planta",
                        modifier = Modifier
                            .size(200.dp)
                            .clickable { navController.navigate("segunda_planta") }
                            .padding(bottom = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { navController.navigate("publicaciones") },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Ir a publicaciones")
                }
            } else {
                Text(text = "Error al cargar las imágenes", color = Color.Red)
            }

            Button(
                onClick = {
                    navController.navigate("iniciar_sesion") {
                        popUpTo("iniciar_sesion") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}