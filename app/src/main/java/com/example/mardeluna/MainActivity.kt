package com.example.mardeluna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.mardeluna.navigation.AppNavigation
import com.example.mardeluna.ui.theme.MarDeLunaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarDeLunaTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}
