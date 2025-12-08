package com.example.levelup.ui

sealed class Screen(val route: String, val label: String, val icon: String) {
    object Home : Screen("home", "Home", "🏠")
    object Manage : Screen("manage", "Manage", "⚙️")
    object Progress : Screen("progress", "Progress", "📈")
    object Community : Screen("community", "Community", "👥")
    object Settings : Screen("settings", "Settings", "🔧")
}
