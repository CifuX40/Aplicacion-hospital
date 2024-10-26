package modelo

import android.content.Context
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class ClaseConexion {
    // Método para establecer la conexión
    fun cadenaConexion(context: Context): Connection? {
        val url = "jdbc:oracle:thin:@//localhost:1521/XE"  // Cambia esto si es necesario
        val usuario = "SYSTEM"  // Cambia según tus credenciales
        val contrasena = "SYSTEM"  // Cambia según tus credenciales

        return try {
            DriverManager.getConnection(url, usuario, contrasena)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    // Método para verificar las credenciales del usuario
    fun verificarCredenciales(email: String, contrasena: String, context: Context): Boolean {
        var conexion: Connection? = null
        var resultado: ResultSet? = null
        var isValidUser = false

        try {
            conexion = cadenaConexion(context) // Crear la conexión aquí
            val query = "SELECT * FROM empleados WHERE email = ? AND contrasena = ?"
            val statement: PreparedStatement = conexion!!.prepareStatement(query)
            statement.setString(1, email)
            statement.setString(2, contrasena)
            resultado = statement.executeQuery()

            // Si hay resultados, el usuario es válido
            isValidUser = resultado.next()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            resultado?.close()
            conexion?.close()
        }

        return isValidUser
    }
}
