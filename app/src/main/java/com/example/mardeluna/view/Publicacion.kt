package com.example.mardeluna.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import java.util.*

// Modelo de datos para una publicación
data class Publicacion(
    val id: String = "",
    val imagen: String = "",
    val texto: String = "",
    val usuario: String = "",
    val fecha: Long = System.currentTimeMillis()
)

// Función para agregar una publicación a Firebase
suspend fun agregarPublicacion(imagen: String?, texto: String?) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val usuarioActual = auth.currentUser

    if (usuarioActual != null) {
        val publicacionId = UUID.randomUUID().toString()
        val usuarioNombre =
            usuarioActual.displayName ?: usuarioActual.email ?: "Usuario desconocido"
        val fecha = System.currentTimeMillis()

        val publicacion = Publicacion(
            id = publicacionId,
            imagen = imagen ?: "",
            texto = texto ?: "",
            usuario = usuarioNombre,
            fecha = fecha
        )

        db.collection("Publicación").document(publicacionId).set(publicacion)
        println("Publicación guardada exitosamente con ID: $publicacionId")
    } else {
        println("Error: Usuario no autenticado.")
    }
}

// Función para obtener las publicaciones desde Firestore
@Composable
fun ObtenerPublicaciones() {
    val db = FirebaseFirestore.getInstance()
    val publicaciones = remember { mutableStateListOf<Publicacion>() }
    val isLoading = remember { mutableStateOf(true) }
    val noPublicaciones = remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        // Consultar publicaciones de Firestore ordenadas por fecha (descendente)
        db.collection("Publicación")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                publicaciones.clear()
                if (querySnapshot.isEmpty) {
                    noPublicaciones.value = true
                } else {
                    for (document in querySnapshot) {
                        val publicacion = document.toObject(Publicacion::class.java)
                        publicaciones.add(publicacion)
                    }
                    noPublicaciones.value = false
                }
                isLoading.value = false
            }
            .addOnFailureListener {
                println("Error obteniendo publicaciones: ${it.message}")
                isLoading.value = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            if (noPublicaciones.value) {
                Text(
                    "No hay publicaciones",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Acción para agregar publicación */ },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Crear una publicación")
                }
            } else {
                publicaciones.forEach { publicacion ->
                    PublicacionView(publicacion)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun PublicacionView(publicacion: Publicacion) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = publicacion.usuario, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = publicacion.texto, style = MaterialTheme.typography.bodyMedium)
        if (publicacion.imagen.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(publicacion.imagen),
                contentDescription = "Imagen de publicación",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun AgregarPublicacionUI(onAgregarClick: (String, String) -> Unit) {
    var texto by remember { mutableStateOf(TextFieldValue("")) }
    var imagenUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Agregar Publicación", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = imagenUrl,
            onValueChange = { imagenUrl = it },
            label = { Text("URL de la imagen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(50.dp)
                .background(Color.LightGray)
        )

        if (imagenUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(imagenUrl),
                contentDescription = "Vista previa de la imagen",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        TextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Escribe tu publicación...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAgregarClick(imagenUrl, texto.text) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Agregar Publicación", color = Color.White)
        }
    }
}

@Composable
fun AgregarPublicacionScreen() {
    val texto = remember { mutableStateOf(TextFieldValue("")) }
    val imagenUrl = remember { mutableStateOf("") }

    // Llamar a la función de agregar publicación fuera del contexto composable
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AgregarPublicacionUI { imagen, texto ->
            scope.launch {
                agregarPublicacion(imagen, texto)
            }
        }
    }
}