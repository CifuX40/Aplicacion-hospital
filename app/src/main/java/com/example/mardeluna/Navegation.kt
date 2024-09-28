package com.example.mardeluna

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mardeluna.screens.*

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("main_logo") { MainLogoScreen(navController) }
        composable("second_floor") { SecondFloorScreen(navController) }
        composable("icu_screen") { ICUScreen(navController) }
        composable("SurgeryScreen") { SurgeryScreen(navController) }
        composable("HospitalizationScreen") { HospitalizationScreen(navController) }
        composable("room_screen") { RoomScreen(navController) }
        composable("patient_aspiration_screen") { PatientAspirationScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("first_floor") {
            FirstFloorScreen(navController)
        }

    }
}
