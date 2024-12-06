package com.example.mardeluna.view

import android.util.*
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
fun UciPostquirurgica(navController: NavHostController) {
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var roomImageUrl by remember { mutableStateOf<String?>(null) }
    var loadError by remember { mutableStateOf(false) }

    // Descargar la URL de la imagen de fondo
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        // Cargar fondo
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                Log.d("Firebase", "URL de fondo obtenida: $imageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al obtener URL del fondo: ${exception.message}")
            }

        // Cargar imagen de la sala UCI
        val roomRef = storage.reference.child("sala_uci_dos.jpg")
        roomRef.downloadUrl
            .addOnSuccessListener { uri ->
                roomImageUrl = uri.toString()
                Log.d("Firebase", "URL de la sala obtenida: $roomImageUrl")
            }
            .addOnFailureListener { exception ->
                loadError = true
                Log.e("Firebase", "Error al obtener URL de la sala: ${exception.message}")
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
                    IconButton(onClick = { navController.navigate("main_logo") }) {
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
                // Imagen de fondo
                if (imageUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUrl),
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

                // Contenido principal
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "UCI Postquirúrgica",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (roomImageUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(model = roomImageUrl),
                            contentDescription = "Sala UCI",
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "CRITERIO DE INGRESO EN REA-UCI DE PACIENTES EN POSTUIRÚRGICO INMEDIATO\n" +
                                "Desde un punto de vista conceptual esta necesidad de vigilancia debe establecerse siguiendo dos criterios fundamentales:\n" +
                                "1.- Complejidad de la cirugía: Se establecen complejidades de grado I a Grado IV y se establece en cuales es necesario un dispositivo de Cuidados Mínimos (despertar), Reanimación postquirúrgica y/o Unidad de Cuidados Intensivos Postquirúrgicos permanente.\n" +
                                "2.- Riesgo anestésico: La escala habitualmente utilizada es la denominada ASA (American\n" +
                                "Society of Anaesthesiologists ).\n" +
                                "\n" +
                                "Grado de complejidad quirúrgica o riesgo quirúrgico.\n" +
                                "\n" +
                                "Grado \tDefinición\n" +
                                "\tPROCEDIMIENTOS MENORES: Escasa agresividad quirúrgica, en zonas con escaso riesg e sangrado o en caso de producirse, fácilmente detectable\n" +
                                "\tPROCEDIMIENTOS MEDIANOS: Mayor probabilidad de hemorragia y mayor riesgo d pasar inadvertida (cavidades).\n" +
                                "III\tPROCEDIMIENTOS MAYORES: mayor agresión quirúrgica y postoperatorio estimad prolongado\n" +
                                "\tPROCEDIMIENTOS MUY RELEVANTES: Aquellos que en el postoperatorio requiere uidados críticos o muy especializados\n" +
                                "Listado orientativo (no exhaustivo) de algunas de las cirugías más relevantes por especialidades:\n" +
                                "GRADO I\n" +
                                " \tCirugía general: Proctología (hemorroidectomía M-M fistulectomía, ELI), exéresis de lipomas (<6cm o subcutáneos), adenopatías accesibles a la palpación, fibroadenomas,  quiste pilonidal, Ca. Basocelulares de piel y cara, hernioplastias abiertas, colocación de PAC.\n" +
                                " \tCOT: Artroscopia (excepto espalda),dedos, túnel carpiano, Dupuytren, hallux valgus, reducción cerrada de fracturas o con aguja Kirschner, reconstrucción de partes blandas .\n" +
                                " \tCirugía maxilofacial: cordalectomía.\n" +
                                " \tCirugía vascular: amputación de dedos.\n" +
                                " \tGinecología: quiste de Bartholino, histeroscopia.\n" +
                                " \tOftalmología: cataratas, corrección de estrabismo, glaucoma, .\n" +
                                " \tORL: miringoplastia, drenaje timpánico, revisión de cadena, microcirugía laríngea.    Urología: orquidopexia, fimosis, vasectomía, biopsia de próstata, hidrocele, varicocele.\n" +
                                "GRADO ll\n" +
                                " \tCirugía general: apendicectomía, hemitiroidectomía, colecistectomía, piloroplastia, mastectomía, prótesis mamaria, eventración, laparoscopia diagnóstica, colocación de drenaje torácico (en este caso, siempre debería existir profesional médico capacitado para su vigilancia y modificación).\n" +
                                " \tCOT: osteosíntesis (excepto fémur), espalda (incluye artroscopia), plastia ligamento cruzado.\n" +
                                " \tCirugía vascular: safenectomía, amputación transmetatarsiana, embolectomía.   Ginecología: quiste de ovario, anexectomía, corrección de cistocele, laparoscopia diagnóstica, histerectomía abdominal o vaginal (excluye neoplasia).\n" +
                                " \tOftalmología: dacriocistectomía, desprendimiento de retina, evisceración ocular.  \tORL:\tadenoidectomía,\tamigdalectomía,\tseptoplastia,\trinoseptoplastia,\tsenos, traqueotomía.\n" +
                                " \tUrología: cistoscopia, RTU, corrección de cistocele\n" +
                                "GRADO III\n" +
                                " \tCirugía general y digestiva: neoplasias intestinales, tiroidectomía total,  linfadenectomía regional, quiste hidatídico hepático, biopsia, hepática, gastrectomía, esplenectomía, resección intestinal, cirugía de la obesidad (sleeve gástrico).\n" +
                                " \tCOT: prótesis de cadera, prótesis de rodilla, osteosíntesis de fémur, artrodesis columna\n" +
                                " \tCirugía maxilofacial: exéresis neoplasias, fracturas complejas, reconstrucción tumores, parotidectomías, lobectomías.\n" +
                                " \tGinecología: miomectomías, tumores uterinos, cirugía ginecológica reconstructiva.\n" +
                                " \tUrología: prostatectomía.\n" +
                                "GRADO IV\n" +
                                " \tCirugía mayor: resección oncológica mayor, trasplante de órganos.\n" +
                                " \tCirugía cardíaca y torácica: cardio bypass, transplante pulmonar.\n" +
                                " \tOtras cirugías de alta complejidad.\n"
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                navController.navigate("respirador")
                            },
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text("Respirador Savina 600")
                        }

                        Button(
                            onClick = {
                                navController.navigate("carro_ingresos")
                            }
                        ) {
                            Text("Carro de ingresos")
                        }
                    }
                }
            }
        }
    )
}