package com.example.gpsemulator.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gpsemulator.ui.home.HomeScreen
import com.example.gpsemulator.ui.search.SearchScreen
import com.example.gpsemulator.ui.settings.SettingsScreen

@Composable
fun MainNavigation(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            // Check for search result
            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            val placeId = savedStateHandle?.get<String>("placeId")
            
            // In a real app, we would fetch Place details (LatLng) here using placeId
            // For now, let's assume we can pass lat/lng or just log it. 
            // Better approach: HomeViewModel observes this.
            
            HomeScreen(
                onNavigateToSearch = { navController.navigate("search") },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        composable("search") {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onPlaceSelected = { placeId ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("placeId", placeId)
                    navController.popBackStack()
                }
            )
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}
