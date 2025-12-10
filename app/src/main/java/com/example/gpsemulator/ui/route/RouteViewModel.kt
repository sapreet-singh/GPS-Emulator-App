package com.example.gpsemulator.ui.route

import android.app.Application
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.gpsemulator.data.service.MockLocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RouteViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    private val _startLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val startLocation = _startLocation.asStateFlow()

    private val _endLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val endLocation = _endLocation.asStateFlow()

    fun setStart(lat: Double, lng: Double) {
        _startLocation.value = lat to lng
    }

    fun setEnd(lat: Double, lng: Double) {
        _endLocation.value = lat to lng
    }

    fun startSimulation(duration: Int = 60) {
        val start = _startLocation.value ?: return
        val end = _endLocation.value ?: return

        val intent = Intent(application, MockLocationService::class.java).apply {
            action = MockLocationService.ACTION_START_ROUTE
            putExtra(MockLocationService.EXTRA_LAT, start.first)
            putExtra(MockLocationService.EXTRA_LNG, start.second)
            putExtra(MockLocationService.EXTRA_END_LAT, end.first)
            putExtra(MockLocationService.EXTRA_END_LNG, end.second)
            putExtra(MockLocationService.EXTRA_DURATION, duration)
        }
        application.startForegroundService(intent)
    }
}
