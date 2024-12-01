package com.example.mardeluna.view

import android.content.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.compose.*
import com.example.mardeluna.R
import com.google.firebase.auth.*
import com.google.firebase.storage.*

@Composable
fun StartScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()

    // Recordar las cuentas utilizadas
    val sharedPrefs: SharedPreferences =
        LocalContext.current.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    val savedEmails = remember {
        mutableStateOf(
            sharedPrefs.getStringSet("emails", setOf())?.toMutableSet() ?: mutableSetOf()
        )
    }

    // Guardar las contraseñas en una lista asociada a los correos electrónicos
    val savedPasswords = remember {
        mutableStateOf(
            savedEmails.value.map { sharedPrefs.getString(it, "") ?: "" }.toMutableList()
        )
    }

    var backgroundUrl by remember { mutableStateOf("") }
    var loadError by remember { mutableStateOf(false) }

    // Cargar imagen de fondo
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val backgroundRef = storage.reference.child("fondo_de_pantalla.jpg")
        backgroundRef.downloadUrl
            .addOnSuccessListener { uri -> backgroundUrl = uri.toString() }
            .addOnFailureListener { loadError = true }
    }

    // Función para iniciar sesión
    fun loginUser() {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Guardar el email en SharedPreferences para recordarlo la próxima vez
                        savedEmails.value.add(email)
                        sharedPrefs.edit().putStringSet("emails", savedEmails.value).apply()

                        // Guardar la contraseña asociada al email
                        sharedPrefs.edit().putString(email, password).apply()

                        if (email == "admin@mardeluna.com") {
                            navController.navigate("admin") {
                                popUpTo("start") { inclusive = true }
                            }
                        } else {
                            navController.navigate("main_logo") {
                                popUpTo("start") { inclusive = true }
                            }
                        }
                    } else {
                        loginError = "Error al iniciar sesión: ${task.exception?.message}"
                    }
                }
        } else {
            loginError = "Por favor ingresa correo y contraseña"
        }
    }

    // Función para autocompletar campos
    fun autofillFields(selectedEmail: String) {
        email = selectedEmail
        val selectedIndex = savedEmails.value.indexOf(selectedEmail)
        if (selectedIndex >= 0) {
            password = savedPasswords.value[selectedIndex]
        }
    }

    // Función para eliminar una cuenta guardada
    fun removeEmail(emailToRemove: String) {
        savedEmails.value.remove(emailToRemove)
        val indexToRemove = savedEmails.value.indexOf(emailToRemove)
        if (indexToRemove >= 0) {
            savedPasswords.value.removeAt(indexToRemove)
        }
        sharedPrefs.edit().putStringSet("emails", savedEmails.value).apply()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla
        if (backgroundUrl.isNotEmpty() && !loadError) {
            Image(
                painter = rememberAsyncImagePainter(backgroundUrl),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (loadError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
        }

        // Contenido de la pantalla de inicio de sesión
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido al hospital Mar de Luna",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Logo de la app
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )

            // Botón de la historia del hospital
            Button(
                onClick = { navController.navigate("history") },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "Historia del Hospital")
            }

            // Campos de correo y contraseña
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar error si existe
            if (loginError.isNotEmpty()) {
                Text(
                    text = loginError,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de inicio de sesión
            Button(
                onClick = { loginUser() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "Iniciar sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar botones de cuentas guardadas
            if (savedEmails.value.isNotEmpty()) {
                Text(text = "Cuentas guardadas", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))

                savedEmails.value.forEach { storedEmail ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botón para autocompletar campos
                        Button(
                            onClick = { autofillFields(storedEmail) },
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Text(text = storedEmail, maxLines = 1)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Botón para eliminar cuenta
                        IconButton(
                            onClick = { removeEmail(storedEmail) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}