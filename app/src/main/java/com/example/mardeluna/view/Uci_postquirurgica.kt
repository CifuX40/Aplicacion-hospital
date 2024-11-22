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
import coil.compose.*
import com.google.firebase.storage.*
import androidx.compose.ui.layout.*

@Composable
fun UciPostquirurgicaScreen(navController: NavHostController) {
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

    Box(
        modifier = Modifier.fillMaxSize()
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
            // Título en negrita
            Text(
                text = "UCI Postquirúrgica",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Texto descriptivo (sin negrita)
            Text(
                text = "CRITERIO DE INGRESO EN REA-UCI DE PACIENTES EN POSTUIRÚRGICO INMEDIATO\n" +
                        "\n" +
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
                        " \tCirugía maxilofacial: exéresis neoplasias\n" +
                        " \tCirugía vascular: cirugía carotídea, amputación de extremidades, derivación by-pass  \tGinecología: histerectomía con anexectomía,\tneoplasias sin necesidad de linfadenectomía asociada.  \tORL: neoplasias, SAOS\n" +
                        " \tUrología: adenomectomía prostática retropúbica, cistectomía, nefrectomía cirugía renal.\n" +
                        "GRADO IV\n" +
                        " \tCirugía general: gran cirugía neoplásica (pelvi-peritonectomía), Colectomía total o subtotal, hepatectomía mayor. Cirugía hepato-bilio-pancreática, Obesidad mórbida\n" +
                        " \n" +
                        "(bypass gástrico)\n" +
                        " \tCOT: recambio de prótesis de cadera  \tCirugía vascular: cirugía aórtica.\n" +
                        " \tCirugía Torácica: Neumonectomía, trasplante pulmonar   Cirugía Cardiaca: en general.\n" +
                        " \tGinecología: cirugía neoplásica agresiva (con linfadenectomía, con necesidad de cirugía sobre metástasis peritoneales, etc.).\n" +
                        " \tNeurocirugía: cirugía intracraneal, tumores raquídeos.\n" +
                        " \tUrología: cistectomía o prostatectomía radical, trasplante renal.\n" +
                        "Clasificación ASA riesgo anestésico\n" +
                        "Tabla 1 . Clasificación perioperatoria según el estado ( Sociedad Americana de Anestesiología)\tico\n" +
                        "Grado\tCaracterísticas del paciente\n" +
                        "Normal, sano-\n" +
                        "Con enfermedad sistémica moderada a leve, sin limitaciones funcionales \n" +
                        "Con enfermedad sistémica moderada a grave, limitante, pero no incapacitante. Con enfermedad sistémica grave incapacitante, con amenaza para su vida\n" +
                        "Moribundo, que no se espera que sobreviva 24 horas, con cirugía o sin ella\n" +
                        "Con muerte cerebral, cuyos órganos se toman para trasplante\n" +
                        "Si la cirugía es de urgencia, se añadirá una U al estado físico (por ejemplo, IU)-\n" +
                        " \n" +
                        "Protocolo de ingreso en unidad de UVI o REA posquirúrgica.\n" +
                        "A.- NO VIGILANCIA (pueden pasar a la planta, aunque siempre debe existir disponible una REA o despertar con anestesista que sea capaz de realizar vigilancia durante un periodo de tiempo que no exceda las 6 horas):\n" +
                        "\t \tComplejidad quirúrgica menor (Grado 1- 1 1 ) y ASA 1- 1 1 .\n" +
                        "B.- VIGILANCIA hasta 12 horas (REANIMACIÓN POST-QUIRÚRGICA):\n" +
                        " \tComplejidad quirúrgica 1 1 -1 1 1 y riesgo anestésico: o Pacientes mayores de 65 años ASA ll o Pacientes menores de 65 años hasta ASA III\n" +
                        "C.-VIGILANCIA DE 12 a 24 HORAS (REANIMACIÓN POST-QUIRÚRGICA)\n" +
                        "\t \tComplejidad quirúrgica III con ASA hasta III.\n" +
                        "  Complejidad quirúrgica IV, con ASA hasta III: En estos casos, se valorarán excepciones en cirugías programadas, en procedimientos muy estandarizados en el proceder quirúrgico, y con equipos quirúrgicos muy experimentados en las mismas. (cirugía de cadera sin ser recambio, by-pass gástrico).\n" +
                        "D.-VIGILANCIA de más de 24 horas (UCI permanente)\n" +
                        "  Complejidad quirúrgica IV   Riesgo anestésico:\n" +
                        "o ASA IV y superiores siempre, independientemente de la edad\n" +
                        " Procedimientos quirúrgicos oncológicos (cirugía oncológica)\n" +
                        " Procedimientos quirúrgicos urgentes con inestabilidad hemodinámica previa (los urgentes sin inestabilidad hemodinámica se deberán incluir en su categoría propia, independientemente de la urgencia)\n" +
                        "\t \tCirugía mayor, con apertura de cavidades.\n" +
                        "Eventos excepcionales que requieren ingreso en UCI o REA (Criterios C y D)\n" +
                        "Existen eventos adversos durante la realización de la anestesia y el procedimiento quirúrgico que cambiarán, sin duda, la necesidad previamente establecida de cuidados postoperatorios. Entre ellos los siguientes:\n" +
                        " \tCirugía urgente durante la cual ha existido inestabilidad hemodinámica de difícil corrección.\n" +
                        " Cirugía programada con efecto quirúrgico adverso inesperado que puede suponer inestabilidad hemodinámica en las primeras 24-48h.\n" +
                        "  Cirugía con prolongación del tiempo quirúrgico por encima de una hora del tiempo inicialmente esperado.\n" +
                        "  Evento adverso inesperado no relacionado con la cirugía durante la inducción  anestésica o el despertar ( shock anafiláctico, broncoespasmo, angina, IAM, crisis comicial, etc.) con gravedad clínica que no se recupera en la primera hora.\n" +
                        "La actividad obstétrica, por la necesidad no esperable de intervenciones complejas para la recuperación de problemas médico-quirúrgicos, debería hacerse siempre en centros que al  menos contasen con la posibilidad de una REA nivel C.\n" +
                        "Todos estos casos, tras la cirugía, en caso de no existir UCI, deberán pasar al despertar/REA con vigilancia anestésica, y en el menor tiempo posible, trasladarse a un centro con UCI para su mayor seguridad.\n" +
                        "En aquellos centros sin UCI, y por tanto, sin presencia de especialistas en Medicina Intensiva,  sería recomendable a pesar del mayor coste probable, contratar especialistas en la citada rama para el trabajo en el Servicio de Urgencias.",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Imagen de la sala UCI
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

            // Botones uno debajo del otro
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        // Acción para "Respirador Savina 300"
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text("Respirador Savina 300")
                }

                Button(
                    onClick = {
                        // Acción para "Carro de ingresos"
                    }
                ) {
                    Text("Carro de ingresos")
                }
            }
        }
    }
}
