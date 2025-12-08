package com.example.levelup.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.data.AppDatabaseProvider
import com.example.levelup.data.AuthRepository
import com.example.levelup.data.HabitRepository

@Composable
fun RootApp() {
    val rootNavController = rememberNavController()
    val context = LocalContext.current

    // Build Room DB and repositories once
    val db = remember { AppDatabaseProvider.getDatabase(context) }
    val habitRepository = remember { HabitRepository(db.habitDao()) }
    val authRepository = remember { AuthRepository(db.userDao()) }

    NavHost(
        navController = rootNavController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeAuthScreen(
                onLoginClick = { rootNavController.navigate("login") },
                onRegisterClick = { rootNavController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                authRepository = authRepository,
                onRegistrationSuccess = {
                    rootNavController.popBackStack()
                }
            )
        }

        composable("login") {
            LoginScreen(
                authRepository = authRepository,
                onLoginSuccess = {
                    rootNavController.navigate("main") { 
                        popUpTo("welcome") { inclusive = true } 
                    }
                }
            )
        }

        composable("main") {
            LevelUpApp(
                habitRepository = habitRepository,
                authRepository = authRepository,
                onLogout = {
                    rootNavController.navigate("welcome") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}
