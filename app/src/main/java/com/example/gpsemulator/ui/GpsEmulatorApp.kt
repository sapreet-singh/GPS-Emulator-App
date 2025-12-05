package com.example.gpsemulator.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.gpsemulator.navigation.AppNavigation
import com.example.gpsemulator.ui.theme.GpsEmulatorTheme

@Composable
fun GpsEmulatorApp() {
    GpsEmulatorTheme {
        val navController = rememberNavController()
        AppNavigation(navController = navController)
    }
}
