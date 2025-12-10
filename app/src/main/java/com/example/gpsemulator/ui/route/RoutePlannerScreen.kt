package com.example.gpsemulator.ui.route

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RoutePlannerScreen(
    viewModel: RouteViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val start by viewModel.startLocation.collectAsState()
    val end by viewModel.endLocation.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Route Planner")
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = "Start: ${start?.let { "${it.first}, ${it.second}" } ?: "Not Selected"}")
        Button(onClick = { 
            // Mock selection for now
            viewModel.setStart(37.7749, -122.4194) 
        }) {
            Text("Select Start (SF)")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(text = "End: ${end?.let { "${it.first}, ${it.second}" } ?: "Not Selected"}")
        Button(onClick = { 
            // Mock selection for now
            viewModel.setEnd(34.0522, -118.2437) 
        }) {
            Text("Select End (LA)")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { viewModel.startSimulation() },
            enabled = start != null && end != null
        ) {
            Text("Start Simulation")
        }
        
        Button(onClick = onNavigateBack) {
            Text("Back")
        }
    }
}
