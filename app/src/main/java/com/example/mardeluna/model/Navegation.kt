package com.example.mardeluna.model

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mardeluna.view.AspiracionScreen
import com.example.mardeluna.view.CarinaScreen
import com.example.mardeluna.view.CarroParadasScreen
import com.example.mardeluna.view.ControlEnfermeriaScreen
import com.example.mardeluna.view.Endoscopias
import com.example.mardeluna.view.Endoscopios
import com.example.mardeluna.view.FirstFloorScreen
import com.example.mardeluna.view.HistoryScreen
import com.example.mardeluna.view.HospitalDeDiaScreen
import com.example.mardeluna.view.HospitalizationScreen
import com.example.mardeluna.view.ICUScreen
import com.example.mardeluna.view.Lavado
import com.example.mardeluna.view.MainLogoScreen
import com.example.mardeluna.view.PaginasWebScreen
import com.example.mardeluna.view.RCPScreen
import com.example.mardeluna.view.ResiduosHospitalariosScreen
import com.example.mardeluna.view.RespiradorScreen
import com.example.mardeluna.view.RoomScreen
import com.example.mardeluna.view.SecondFloorScreen
import com.example.mardeluna.view.StartScreen
import com.example.mardeluna.view.SurgeryScreen
import com.example.mardeluna.view.TomaOxigenoScreen
import com.example.mardeluna.view.UciMedicaScreen
import com.example.mardeluna.view.UciPostquirurgicaScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("main_logo") { MainLogoScreen(navController) }
        composable("second_floor") { SecondFloorScreen(navController) }
        composable("icu_screen") { ICUScreen(navController) }
        composable("surgery_screen") { SurgeryScreen(navController) }
        composable("hospitalization_screen") { HospitalizationScreen(navController) }
        composable("room_screen") { RoomScreen(navController) }
        composable("first_floor") { FirstFloorScreen(navController) }
        composable("aspiracion_screen") { AspiracionScreen(navController) }
        composable("toma_oxigeno_screen") { TomaOxigenoScreen(navController) }
        composable("carina_screen") { CarinaScreen(navController) }
        composable("respirador_screen") { RespiradorScreen(navController) }
        composable("uci_postquirurgica") { UciPostquirurgicaScreen(navController) }
        composable("uci_medica") { UciMedicaScreen(navController) }
        composable("endoscopias_screen") { Endoscopias(navController) }
        composable("hospital_dia_screen") { HospitalDeDiaScreen(navController) }
        composable("endoscopios_screen") { Endoscopios(navController) }
        composable("lavadora_screen") { Lavado(navController) }
        composable("carro_paradas") { CarroParadasScreen(navController) }
        composable("control_enfermeria") { ControlEnfermeriaScreen(navController) }
        composable("paginas_web_screen") { PaginasWebScreen(navController) }
        composable("rcp_screen") { RCPScreen(navController) }
        composable("residuos_hospitalarios_screen") { ResiduosHospitalariosScreen(navController) }
    }
}