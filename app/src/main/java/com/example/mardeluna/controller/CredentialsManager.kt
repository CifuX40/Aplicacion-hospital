package com.example.mardeluna.controller

import android.content.*

class CredentialsManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE)

    fun saveUser(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(email, password)
        editor.apply()
    }

    fun getAllUsers(): Map<String, String> {
        return sharedPreferences.all.mapNotNull {
            val email = it.key
            val password = it.value as? String
            if (password != null) email to password else null
        }.toMap()
    }

    fun clearUser(email: String) {
        sharedPreferences.edit().remove(email).apply()
    }
}