package com.example.mardeluna.view

import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.navigation.compose.*
import com.example.mardeluna.model.AppNavigation
import com.example.mardeluna.ui.theme.MarDeLunaTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MarDeLunaTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}