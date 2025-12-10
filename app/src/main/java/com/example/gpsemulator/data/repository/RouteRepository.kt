package com.example.gpsemulator.data.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// Simple in-memory route simulation
@Singleton
class RouteRepository @Inject constructor() {
    
    // Simulate route: Interpolate between A and B
    fun simulateRoute(startLat: Double, startLng: Double, endLat: Double, endLng: Double, durationSeconds: Int): Flow<Pair<Double, Double>> = flow {
        val steps = durationSeconds // 1 step per second
        val stepLat = (endLat - startLat) / steps
        val stepLng = (endLng - startLng) / steps
        
        for (i in 0..steps) {
            val validLat = startLat + (stepLat * i)
            val validLng = startLng + (stepLng * i)
            emit(validLat to validLng)
            kotlinx.coroutines.delay(1000)
        }
    }
}
