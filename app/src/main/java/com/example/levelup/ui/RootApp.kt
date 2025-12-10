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
import com.example.levelup.data.LeaderboardRepository

@Composable
fun RootApp() {
    val rootNavController = rememberNavController()
    val context = LocalContext.current

    // --- Build Room DB Repositories once ---
    val db = remember { AppDatabaseProvider.getDatabase(context) }
    val habitRepository = remember { HabitRepository(db.habitDao()) }
    val authRepository = remember { AuthRepository(db.userDao()) }
    val leaderboardRepository = remember { LeaderboardRepository() }

    // --- Navigation graph ---
    NavHost(
        navController = rootNavController,
        startDestination = "welcome"
    ) {
        // Welcome screen (Log In / Register)
        composable("welcome") {
            WelcomeAuthScreen(
                onLoginClick = { rootNavController.navigate("login") },
                onRegisterClick = { rootNavController.navigate("register") }
            )
        }

        // Registration Screen
        composable("register") {
            RegisterScreen(
                authRepository = authRepository,
                onRegistrationSuccess = {
                    rootNavController.popBackStack()
                }
            )
        }

        // Login Screen
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
                leaderboardRepository = leaderboardRepository,
                onLogout = {
                    rootNavController.navigate("welcome") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}
