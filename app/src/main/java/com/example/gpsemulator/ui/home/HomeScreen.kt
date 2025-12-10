package com.example.gpsemulator.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val isMocking by viewModel.isMocking.collectAsState()
    val currentMockLocation by viewModel.currentMockLocation.collectAsState()
    val mapCenter by viewModel.mapCenter.collectAsState()
    
    val openPermissionDialog = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            if (event is com.example.gpsemulator.data.repository.EmulationEvent.PermissionDenied) {
                openPermissionDialog.value = true
            }
        }
    }
    
    if (openPermissionDialog.value) {
        AlertDialog(
            onDismissRequest = { openPermissionDialog.value = false },
            title = { Text("Mock Location Permission Required") },
            text = { Text("Please enable 'GPS Emulator' as the Mock Location app in Developer Options.") },
            confirmButton = {
                TextButton(onClick = { 
                    openPermissionDialog.value = false
                    // Ideally open Developer Options here
                }) {
                    Text("OK")
                }
            }
        )
    }

    val cameraPositionState = rememberCameraPositionState()
    
    // Update camera when mapCenter changes (initial load)
    // In a real app, use LaunchedEffect to animate camera

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GPS Emulator") },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val target = cameraPositionState.position.target
                    if (isMocking) {
                        viewModel.stopMocking()
                    } else {
                        viewModel.startMocking(target.latitude, target.longitude)
                    }
                }
            ) {
                Icon(
                    if (isMocking) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = if (isMocking) "Stop" else "Start"
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    if (isMocking) {
                         viewModel.updateLocation(latLng.latitude, latLng.longitude)
                    } else {
                        // Just move camera or marker?
                        // For now, let's say click sets the target for next start
                    }
                }
            ) {
                if (isMocking && currentMockLocation != null) {
                    Marker(
                        state = MarkerState(position = LatLng(currentMockLocation!!.first, currentMockLocation!!.second)),
                        title = "Mock Location"
                    )
                }
            }
        }
    }
}
