package com.example.mardeluna

import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.navigation.compose.*
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