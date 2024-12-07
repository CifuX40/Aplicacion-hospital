package com.example.mardeluna.model

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.mardeluna.view.*

// Función de navegación
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "start") {
        composable("agregar_publicacion") { AgregarPublicacionUI(navController) }
        composable("admin") { AdminScreen(navController) }
        composable("aspiracion_screen") { AspiracionScreen(navController) }
        composable("carina_screen") { CarinaScreen(navController) }
        composable("carro_ingresos") { CarroIngresos(navController) }
        composable("carro_paradas") { CarroParadas(navController) }
        composable("chequeo_respirador_screen") { ChequeoRespiradorScreen(navController) }
        composable("controles_carga_autoclaves_screen") { ControlesCargaScreen(navController) }
        composable("control_enfermeria") { ControlEnfermeriaScreen(navController) }
        composable("desfibrilador") { Desfibrilador(navController) }
        composable("empaquetado_screen") { EmpaquetadoScreen(navController) }
        composable("endoscopias_screen") { Endoscopias(navController) }
        composable("endoscopios_screen") { Endoscopios(navController) }
        composable("esterilizacion_screen") { EsterilizacionScreen(navController) }
        composable("evita_600") { Evita600(navController) }
        composable("first_floor") { FirstFloorScreen(navController) }
        composable("habitacion") { Habitacion(navController) }
        composable("historia") { Historia(navController) }
        composable("hoja_informativa_pacientes") { HojaInformativaPacientesScreen(navController) }
        composable("hospital_dia_screen") { HospitalDeDiaScreen(navController) }
        composable("hospitalizacion") { Hospitalizacion(navController) }
        composable("lavadora_screen") { Lavado(navController) }
        composable("main_logo") { MainLogoScreen(navController) }
        composable("paginas_web") { PaginasWeb(navController) }
        composable("procedimiento_ingresos") { ProcedimientoIngresosScreen(navController) }
        composable("publicaciones") { Publicaciones(navController) }
        composable("qoma_constantes") { TomaConstantesScreen(navController) }
        composable("quirófano") { Quirofano(navController) }
        composable("rcp_screen") { RCPScreen(navController) }
        composable("rea_screen") { ReaScreen(navController) }
        composable("residuos_hospitalarios_screen") { ResiduosHospitalariosScreen(navController) }
        composable("sala_quirofano_screen") { SalaQuirofanoScreen(navController) }
        composable("savina") { Savina(navController) }
        composable("second_floor") { SecondFloorScreen(navController) }
        composable("start") { StartScreen(navController) }
        composable("toma_oxigeno_screen") { TomaOxigenoScreen(navController) }
        composable("uci") { Uci(navController) }
        composable("uci_medica") { UciMedica(navController) }
        composable("uci_postquirurgica") { UciPostquirurgica(navController) }
    }
}