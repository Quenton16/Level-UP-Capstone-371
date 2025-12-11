package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.levelup.data.AuthRepository
import com.example.levelup.data.FirebaseAuthHelper
import com.example.levelup.data.User

@Composable
fun WelcomeAuthScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Level UP",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Gamify your habits. Log in or create an account to get started.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log In")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}

@Composable
fun RegisterScreen(
    authRepository: AuthRepository,
    onRegistrationSuccess: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val firebaseHelper = remember { FirebaseAuthHelper() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = dob,
            onValueChange = { dob = it },
            label = { Text("Date of Birth (MM/DD/YYYY)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val validationError = validateRegistration(
                    firstName, lastName, dob, email, password, confirmPassword
                )
                if (validationError != null) {
                    message = validationError
                } else {
                    val user = User(
                        firstName = firstName.trim(),
                        lastName = lastName.trim(),
                        dateOfBirth = dob.trim(),
                        email = email.trim()
                    )

                    firebaseHelper.registerUser(user, password) { success, error ->
                        if (success) {
                            message = "Registration successful. Please log in."
                            onRegistrationSuccess()
                        } else {
                            message = error ?: "Registration failed."
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = if ("failed" in message.lowercase())
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun validateRegistration(
    firstName: String,
    lastName: String,
    dob: String,
    email: String,
    password: String,
    confirmPassword: String
): String? {
    if (firstName.length !in 3..30) {
        return "First name must be between 3 and 30 characters."
    }
    if (lastName.isBlank()) return "Family name cannot be empty."
    if (dob.isBlank()) return "Date of birth cannot be empty."
    if (!email.contains("@") || !email.contains(".")) {
        return "Please enter a valid email."
    }
    if (password.length < 6) return "Password should be at least 6 characters."
    if (password != confirmPassword) return "Passwords do not match."
    return null
}

@Composable
fun LoginScreen(
    authRepository: AuthRepository,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val firebaseHelper = remember { FirebaseAuthHelper() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Log In",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                firebaseHelper.login(email.trim(), password) { success, error ->
                    if (success) {
                        message = ""
                        onLoginSuccess()
                    } else {
                        message = error ?: "Login failed."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log In")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
