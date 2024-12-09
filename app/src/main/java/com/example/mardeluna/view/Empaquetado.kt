package com.example.mardeluna.view

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
import androidx.compose.ui.layout.*
import coil.compose.*
import com.google.firebase.ktx.*
import com.google.firebase.storage.ktx.*
import androidx.compose.ui.text.*

@Composable
fun Empaquetado(navController: NavHostController) {
    var backgroundUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val storage = Firebase.storage
        val backgroundRef =
            storage.getReferenceFromUrl("gs://mar-de-luna-ada79.firebasestorage.app/fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { exception ->
                Log.e(
                    "Firebase",
                    "Error al cargar fondo: ${exception.message}"
                )
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (backgroundUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Empaquetado",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = buildAnnotatedString {
                    append("En función del tipo de instrumental a esterilizar, la forma de envasado será la siguiente: Envasado de instrumental:\n")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Bolsa de papel mixto")
                    }
                    append(": para material individual o cajas de pequeño tamaño. Para instrumentos voluminosos, cortantes o con bordes puntiagudos, se empleará doble bolsa.\n")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Empaquetado barrera")
                    }
                    append(": para cajas de grandes dimensiones o cajas perforadas. Deben envasarse con un sistema de barrera estéril: papel crepado y tejido sin tejer. Por fuera y como protección para su transporte y almacenaje, se debe envolver con otro paño que se cerrará con cinta adhesiva indicadora de vapor.\n")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Contenedor hermético con filtro")
                    }
                    append(": deben tener un cuerpo de una sola pieza donde se colocan los cestillos con el material y una tapa portando la barrera biológica. La junta debe permitir la hermeticidad total entre los dos conjuntos (cuerpo y tapa) y tienen que estar equipados con bridas o asas para que permitan su manejo. El peso máximo de la carga en el contenedor no debe exceder los 101C. Se colocará un precinto de seguridad (asa desechable) que se rompa de forma irreversible para la apertura del contenedor. Si el filtro de la tapa es de papel desechable, éste se cambiará en cada ciclo.\n")
                    append("Envasado de textil:\n")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Bolsa de papel")
                    }
                    append(": para batas, paños, sábanas, gasas y compresas (también se pueden envasar en bolsa de papel mixto).\n")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Empaquetado barrera")
                    }
                    append(": para equipos textiles. Deben envasarse con un sistema de barrera estéril: papel crepado y tejido sin tejer. Por fuera y como protección para su transporte y almacenaje, se debe envolver con otro paño que se cerrará con cinta adhesiva indicadora de vapor.\n")
                    append("Envasado de vendas, goma, caucho, silicona y vidrio:\n")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Bolsa de papel mixto")
                    }
                    append(": empaquetarlos en una sola bolsa.\n")
                    append("El tiempo de caducidad, durante el que se garantiza la esterilidad del producto, depende directamente del tipo de envoltorio con el que ha sido procesado.")
                },
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}