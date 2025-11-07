package com.example.gpsemulator.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    // Add more screens here as needed
}
