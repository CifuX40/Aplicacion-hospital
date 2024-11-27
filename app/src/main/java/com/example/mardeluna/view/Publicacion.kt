package com.example.mardeluna.view

import android.net.*
import android.util.*
import androidx.activity.compose.*
import androidx.activity.result.contract.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.*
import com.google.firebase.storage.*

@Composable
fun PublicacionesScreen(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }
    var publicaciones by remember { mutableStateOf<List<Map<String, Any>>?>(null) }
    var loading by remember { mutableStateOf(true) }

    // Cargar fondo y publicaciones desde Firebase
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Fondo de pantalla
        storage.reference.child("fondo_de_pantalla.jpg").downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { Log.e("Firebase", "Error al cargar fondo: ${it.message}") }

        // Cargar publicaciones
        val db = FirebaseFirestore.getInstance()
        db.collection("publicaciones")
            .orderBy("timestamp", Query.Direction.DESCENDING) // Ordenar por la marca de tiempo
            .get()
            .addOnSuccessListener { snapshot ->
                publicaciones = snapshot.documents.map { it.data ?: emptyMap() }
                loading = false
            }
            .addOnFailureListener {
                publicaciones = emptyList()
                loading = false
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
            Text(
                text = "Publicaciones",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (loading) {
                CircularProgressIndicator()
            } else if (publicaciones.isNullOrEmpty()) {
                Text("No hay publicaciones disponibles.")
            } else {
                publicaciones?.forEach { publicacion ->
                    PublicacionItem(publicacion)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para añadir publicación
            Button(
                onClick = {
                    Log.d("PublicacionesScreen", "Navegando a la pantalla de agregar publicación")
                    navController.navigate("agregar_publicacion")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Añadir publicación")
            }
        }
    }
}

@Composable
fun PublicacionItem(publicacion: Map<String, Any>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
    ) {
        val mensaje = publicacion["mensaje"] as? String ?: "Sin mensaje"
        val imagen = publicacion["imagen"] as? String

        Text(text = mensaje, fontSize = 16.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(8.dp))

        imagen?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Imagen de la publicación",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

@Composable
fun AgregarPublicacionUI(navController: NavHostController) {
    var texto by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Añadir Publicación",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para el mensaje
        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Escribe tu mensaje aquí") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para seleccionar imagen
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleccionar Imagen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar imagen seleccionada
        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para publicar
        Button(onClick = {
            if (texto.isNotBlank() || imageUri != null) {
                publicarPublicacion(texto, imageUri) {
                    successMessage = "Publicación realizada con éxito!"
                    Log.d("AgregarPublicacionUI", "Publicación realizada correctamente")
                    // Navegar explícitamente a la pantalla de publicaciones
                    navController.navigate("publicaciones_screen") {
                        popUpTo("publicaciones_screen") { inclusive = true }
                    }
                }
            } else {
                errorMessage = "Debe ingresar un mensaje o seleccionar una imagen."
            }
        }) {
            Text("Publicar")
        }

        // Mensaje de error
        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Mensaje de éxito
        if (successMessage.isNotBlank()) {
            Text(
                text = successMessage,
                color = Color.Green,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

fun publicarPublicacion(
    mensaje: String,
    imageUri: Uri?,
    onSuccess: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance().reference

    val publicacion = hashMapOf(
        "mensaje" to mensaje,
        "timestamp" to FieldValue.serverTimestamp() // Marcar con la hora exacta en que se publica
    )

    if (imageUri != null) {
        val ref = storage.child("publicaciones/${System.currentTimeMillis()}.jpg")
        ref.putFile(imageUri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { uri ->
                publicacion["imagen"] = uri.toString()
                db.collection("publicaciones").add(publicacion).addOnSuccessListener {
                    onSuccess()
                }
            }
        }
    } else {
        db.collection("publicaciones").add(publicacion).addOnSuccessListener {
            onSuccess()
        }
    }
}