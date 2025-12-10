package com.example.gpsemulator.ui.home

import android.app.Application
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gpsemulator.data.repository.EmulationStateRepository
import com.example.gpsemulator.data.repository.SettingsRepository
import com.example.gpsemulator.data.service.MockLocationService
import com.example.gpsemulator.domain.repository.LocationRepository
import com.example.gpsemulator.data.model.RecentLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val stateRepository: EmulationStateRepository,
    private val settingsRepository: SettingsRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    val isMocking = stateRepository.isMocking.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val currentMockLocation = stateRepository.currentMockLocation.stateIn(viewModelScope, SharingStarted.Lazily, null)
    val events = stateRepository.events
    
    // UI State for map center
    private val _mapCenter = MutableStateFlow<Pair<Double, Double>?>(null)
    val mapCenter = _mapCenter.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.lastLocation.collectLatest {
                if (it != null && _mapCenter.value == null) {
                    _mapCenter.value = it
                }
            }
        }
    }

    fun startMocking(lat: Double, lng: Double) {
        val intent = Intent(application, MockLocationService::class.java).apply {
            action = MockLocationService.ACTION_START
            putExtra(MockLocationService.EXTRA_LAT, lat)
            putExtra(MockLocationService.EXTRA_LNG, lng)
        }
        application.startForegroundService(intent)
        
        viewModelScope.launch {
            settingsRepository.saveLastLocation(lat, lng)
            locationRepository.insertRecent(RecentLocation(latitude = lat, longitude = lng))
        }
    }
    
    fun updateLocation(lat: Double, lng: Double) {
        val intent = Intent(application, MockLocationService::class.java).apply {
            action = MockLocationService.ACTION_UPDATE_LOCATION
            putExtra(MockLocationService.EXTRA_LAT, lat)
            putExtra(MockLocationService.EXTRA_LNG, lng)
        }
        application.startService(intent) // Already foreground
        
        viewModelScope.launch {
            settingsRepository.saveLastLocation(lat, lng)
        }
    }

    fun stopMocking() {
        val intent = Intent(application, MockLocationService::class.java).apply {
            action = MockLocationService.ACTION_STOP
        }
        application.startService(intent)
    }
}
