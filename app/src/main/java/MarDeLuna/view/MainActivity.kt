package MarDeLuna.view

import MarDeLuna.model.Navegacion
import MarDeLuna.theme.MarDeLunaTheme
import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.navigation.compose.*
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