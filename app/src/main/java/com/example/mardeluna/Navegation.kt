package com.example.mardeluna

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*

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
        composable("patient_aspiration_screen") { PatientAspirationScreen(navController) }
        composable("first_floor") { FirstFloorScreen(navController) }
        composable("aspiracion_screen") { AspiracionScreen(navController) }
        composable("toma_oxigeno_screen") { Toma_oxigeno(navController) }
        composable("carina_screen") { CarinaScreen(navController) }
        composable("respirador_screen") { RespiradorScreen(navController) }
        composable("uci_postquirurgica") { UciPostquirurgicaScreen(navController) }
        composable("uci_medica") { UciMedicaScreen(navController) }
        composable("endoscopias_screen") { EndoscopiasScreen(navController) }
        composable("hospital_dia_screen") { HospitalDeDiaScreen(navController) }
    }
}