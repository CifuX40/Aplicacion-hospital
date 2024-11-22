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
fun ICUScreen(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf("") }
    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar imágenes desde Firebase
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
                loadError = true
                Log.e("Firebase", "Error al cargar el fondo: ${exception.message}")
            }

        // Cargar la imagen principal
        val storageRef = storage.reference.child("uci.jpg")
        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "Imagen cargada exitosamente: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al cargar la imagen: ${exception.message}")
            }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Mostrar el fondo de pantalla
        if (backgroundUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(model = backgroundUrl),
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

        // Contenido principal superpuesto al fondo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Unidad de cuidados intensivos",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color(0xFF0D47A1)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!loadError && imageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Sala UCI",
                    modifier = Modifier.size(300.dp)
                )
            } else {
                Text(text = "Error al cargar la imagen", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "lnformación general del Servicio de UCI\n" +
                        "\n" +
                        "En esta UCI intentamos promover acciones de mejora sobre los pacientes y sus familias para que reciban una atención más humana y amable, que disminuya el estrés y el malestar que pueden ir asociados a un ingreso en una UCI. Horario de visitas\n" +
                        "Turno de MAÑANA: Desde las 12.00 a las 14.00 horas\n" +
                        "Coincidiendo con este horario, los familiares recibirán la información directa del médico intensivista.\n" +
                        "Turno de TARDE: Desde las 16.00 a las 19.00 horas\n" +
                        "No obstante, en la UCI valoramos cada paciente individualmente y siguiendo criterios clínicos y/o de otro tipo, la UCI puede autorizar un horario flexible de visitas, dejando para ello las puertas abiertas. Si usted necesita flexibilizar estos horarios podrá acordarlo con la enfermera responsable en base al estado, necesidad de descanso y opinión del paciente; las necesidades familiares y la dinámica del servicio. Los horarios pactados pueden ser modificados en cualquier momento debido a cambios puntuales de estas actividades. Cuando visite a su familiar fuera de los horarios generales, les agradeceríamos que avisara a la enfermera responsable antes de pasar.\n" +
                        "•\tSabemos que una de las necesidades de las familias es la de estar junto a su ser querido, por esta razón disponemos de horarios de visita amplios y que se pueden adaptar a sus necesidades. Sin embargo, usted ha de procurar tomarse tiempo para desconectar y dormir lo suficiente para sentirse descansado. El horario de visita es amplio para que se adapte a las diferentes necesidades, no es ni mucho menos necesario que usted acompañe a su familiar durante todas esas horas. Tenga en cuenta que su familiar también necesita descansar.\n" +
                        "•\tLos familiares deben permanecer en la sala de espera hasta ser avisados por el personal de UCI, dejando el pasillo de acceso a la Unidad libre para situaciones de emergencia.\n" +
                        "•\tRecomendamos la entrada de 2 familiares por habitación, pero podrá algún familiar más consultando antes a la enfermera responsable. Podrán intercambiarse los familiares durante el horario de visitas, realizando dicho cambio fuera de la Unidad.\n" +
                        "•\tLes rogamos que mantengan un tono bajo al conversar para limitar el nivel de ruido en la Unidad evitando, de esta manera, molestar a las personas ingresadas.\n" +
                        "•\tComo medida importante para prevenir las infecciones es preciso que recuerden lavarse las manos con el gel alcohólico antes de entrar y salir de la habitación. En situaciones en las que su familiar requiera alguna medida especial de protección, la enfermera le indicará cuál es.\n" +
                        "•\tEs imprescindible dejar un teléfono de contacto donde poder avisarles sobre cualquier posible cambio.\n" +
                        "ALTA EN LA UNIDAD: Nunca se trasladará a un paciente sin el conocimiento de sus familiares.",
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    Log.d("Navigation", "Navegando a UCI Postquirúrgica")
                    navController.navigate("uci_postquirurgica")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = "UCI postquirúrgica")
            }

            Button(
                onClick = {
                    Log.d("Navigation", "Navegando a UCI Médica")
                    navController.navigate("uci_medica")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = "UCI médica")
            }
        }
    }
}