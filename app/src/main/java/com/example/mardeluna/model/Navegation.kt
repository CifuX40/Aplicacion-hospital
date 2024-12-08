package com.example.mardeluna.model

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.mardeluna.view.*

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "start") {
        composable("agregar_publicacion") { AgregarPublicacionUI(navController) }
        composable("admin") { AdminScreen(navController) }
        composable("aspiracion") { Aspiracion(navController) }
        composable("carina_screen") { CarinaScreen(navController) }
        composable("carro_ingresos") { CarroIngresos(navController) }
        composable("carro_paradas") { CarroParadas(navController) }
        composable("chequeo_respirador") { ChequeoRespirador(navController) }
        composable("controles_carga") { ControlesCarga(navController) }
        composable("control_enfermeria") { ControlEnfermeriaScreen(navController) }
        composable("desfibrilador") { Desfibrilador(navController) }
        composable("empaquetado") { Empaquetado(navController) }
        composable("endoscopias_screen") { Endoscopias(navController) }
        composable("endoscopios_screen") { Endoscopios(navController) }
        composable("equipo_quirofano") { EquipoQuirofano(navController) }
        composable("esterilizacion") { Esterilizacion(navController) }
        composable("evita_600") { Evita600(navController) }
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
        composable("toma_constantes") { TomaConstantes(navController) }
        composable("quirófano") { Quirofano(navController) }
        composable("rcp_screen") { RCPScreen(navController) }
        composable("rea_screen") { ReaScreen(navController) }
        composable("residuos_hospitalarios_screen") { ResiduosHospitalariosScreen(navController) }
        composable("sala_quirofano") { SalaQuirofano(navController) }
        composable("savina") { Savina(navController) }
        composable("second_floor") { SecondFloorScreen(navController) }
        composable("start") { StartScreen(navController) }
        composable("toma_oxigeno") { TomaOxigeno(navController) }
        composable("uci") { Uci(navController) }
        composable("uci_medica") { UciMedica(navController) }
        composable("uci_postquirurgica") { UciPostquirurgica(navController) }
    }
}