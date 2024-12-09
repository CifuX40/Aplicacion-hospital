package com.example.mardeluna.view

import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.navigation.compose.*
import com.example.mardeluna.model.*
import com.example.mardeluna.ui.theme.*
import com.google.firebase.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MarDeLunaTheme {
                val navController = rememberNavController()
                Navegacion(navController)
            }
        }
    }
}