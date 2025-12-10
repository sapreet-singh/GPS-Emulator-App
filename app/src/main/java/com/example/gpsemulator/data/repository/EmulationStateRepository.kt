package com.example.gpsemulator.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmulationStateRepository @Inject constructor() {
    private val _isMocking = MutableStateFlow(false)
    val isMocking = _isMocking.asStateFlow()

    private val _currentMockLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentMockLocation = _currentMockLocation.asStateFlow()

    private val _events = kotlinx.coroutines.flow.MutableSharedFlow<EmulationEvent>()
    val events = _events.asSharedFlow()

    fun setMocking(mocking: Boolean) {
        _isMocking.value = mocking
    }

    fun setLocation(lat: Double, lng: Double) {
        _currentMockLocation.value = lat to lng
    }

    suspend fun emitEvent(event: EmulationEvent) {
        _events.emit(event)
    }
}

sealed class EmulationEvent {
    object PermissionDenied : EmulationEvent()
    object GenericError : EmulationEvent()
}
