package com.example.levelup.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.levelup.data.AuthRepository
import com.example.levelup.data.HabitRepository
import com.example.levelup.data.LeaderboardRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelUpApp(
    habitRepository: HabitRepository,
    authRepository: AuthRepository,
    leaderboardRepository: LeaderboardRepository,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val user by authRepository.loggedInUserFlow.collectAsState()

    val screens = listOf(
        Screen.Home,
        Screen.Manage,
        Screen.Progress,
        Screen.Community,
        Screen.Settings
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (user != null) {
                            "Level UP - ${user!!.firstName}"
                        } else {
                            "Level UP"
                        }
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                screens.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                        },
                        icon = { Text(screen.icon) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(repository = habitRepository)
            }
            composable(Screen.Manage.route) {
                ManageHabitScreen(repository = habitRepository)
            }
            composable(Screen.Progress.route) {
                ProgressScreen(repository = habitRepository)
            }
            composable(Screen.Community.route) {
                CommunityScreen(leaderboardRepository = leaderboardRepository)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    authRepository = authRepository,
                    onLogout = onLogout
                )
            }
        }
    }
}
