package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun cadenaConexion(): Connection? {
        try {
            val url = "jdbc:oracle:thin:localhost:1521:xe"
            val usuario = "SYSTEM"
            val contrasena = "SYSTEM"

            val connection = DriverManager.getConnection(url, usuario, contrasena)
            return connection
        } catch (e: Exception) {
            println("error: $e")
            return null
        }
    }
}