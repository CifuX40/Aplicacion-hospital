package com.example.mardeluna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.mardeluna.ui.theme.MarDeLunaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarDeLunaTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }

        // Llama a la función para conectarte a la base de datos
        conectarBaseDeDatos()
    }

    private fun conectarBaseDeDatos() {
        lifecycleScope.launch {
            val conexion = withContext(Dispatchers.IO) {
                val claseConexion = ClaseConexion()
                // Pasar el contexto a la función de conexión
                claseConexion.cadenaConexion(this@MainActivity)
            }

            if (conexion != null) {
                println("Conexión exitosa!")
                // Aquí puedes realizar operaciones adicionales, como ejecutar consultas
                // Por ejemplo, llamar a verificarCredenciales
                verificarCredenciales(conexion, "usuario@ejemplo.com", "tu_contrasena")
            } else {
                println("Fallo en la conexión.")
            }
        }
    }

    private fun verificarCredenciales(conexion: Connection, email: String, contrasena: String) {
        val query = "SELECT * FROM empleados WHERE email = ? AND contrasena = ?"
        var resultado: ResultSet? = null

        try {
            val statement: PreparedStatement = conexion.prepareStatement(query)
            statement.setString(1, email)
            statement.setString(2, contrasena)
            resultado = statement.executeQuery()

            if (resultado.next()) {
                // Credenciales válidas
                println("Inicio de sesión exitoso. Usuario: ${resultado.getString("nombre")}")
                // Navegar a la pantalla principal (o donde sea necesario)
                // navController.navigate("pantalla_principal") // Si tienes acceso a navController aquí
            } else {
                // Credenciales no válidas
                println("Credenciales inválidas.")
            }
        } catch (e: Exception) {
            println("Error al verificar credenciales: ${e.message}")
        } finally {
            resultado?.close()
            conexion.close() // Cerrar la conexión si ya no la necesitas
        }
    }
}
