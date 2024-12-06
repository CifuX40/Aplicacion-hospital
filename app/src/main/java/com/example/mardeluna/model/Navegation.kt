package com.example.mardeluna.model

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.mardeluna.view.*

// Función de navegación
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("main_logo") { MainLogoScreen(navController) }
        composable("admin") { AdminScreen(navController) }
        composable("second_floor") { SecondFloorScreen(navController) }
        composable("UCI") { Uci(navController) }
        composable("Quirófano") { Quirofano(navController) }
        composable("hospitalizacion") { Hospitalizacion(navController) }
        composable("habitacion") { Habitacion(navController) }
        composable("first_floor") { FirstFloorScreen(navController) }
        composable("aspiracion_screen") { AspiracionScreen(navController) }
        composable("toma_oxigeno_screen") { TomaOxigenoScreen(navController) }
        composable("carina_screen") { CarinaScreen(navController) }
        composable("uci_postquirurgica") { UciPostquirurgica(navController) }
        composable("uci_medica") { UciMedica(navController) }
        composable("endoscopias_screen") { Endoscopias(navController) }
        composable("hospital_dia_screen") { HospitalDeDiaScreen(navController) }
        composable("endoscopios_screen") { Endoscopios(navController) }
        composable("lavadora_screen") { Lavado(navController) }
        composable("carro_paradas") { CarroParadasScreen(navController) }
        composable("control_enfermeria") { ControlEnfermeriaScreen(navController) }
        composable("paginas_web") { PaginasWeb(navController) }
        composable("rcp_screen") { RCPScreen(navController) }
        composable("residuos_hospitalarios_screen") { ResiduosHospitalariosScreen(navController) }
        composable("Toma_constantes") { TomaConstantesScreen(navController) }
        composable("hoja_informativa_pacientes") { HojaInformativaPacientesScreen(navController) }
        composable("procedimiento_ingresos") { ProcedimientoIngresosScreen(navController) }
        composable("respirador") { Respirador(navController) }
        composable("savina_screen") { SavinaScreen(navController) }
        composable("carro_ingresos") { CarroIngresos(navController) }
        composable("desfibrilador") { Desfibrilador(navController) }
        composable("evita_600") { Evita600(navController) }
        composable("esterilizacion_screen") { EsterilizacionScreen(navController) }
        composable("rea_screen") { ReaScreen(navController) }
        composable("sala_quirofano_screen") { SalaQuirofanoScreen(navController) }
        composable("empaquetado_screen") { EmpaquetadoScreen(navController) }
        composable("controles_carga_autoclaves_screen") { ControlesCargaScreen(navController) }
        composable("agregar_publicacion") { AgregarPublicacionUI(navController) }
        composable("PublicacionesScreen") { PublicacionesScreen(navController) }
        composable("equipo_quirofano_screen") { EquipoQuirofanoScreen(navController) }
        composable("chequeo_respirador_screen") { ChequeoRespiradorScreen(navController) }
    }
}