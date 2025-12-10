package com.example.gpsemulator.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.location.provider.ProviderProperties
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.example.gpsemulator.MainActivity
import com.example.gpsemulator.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MockLocationService : Service() {

    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var repository: com.example.gpsemulator.data.repository.EmulationStateRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private var isMocking = false
    private var currentLocation: Location? = null

    @Inject
    lateinit var routeRepository: com.example.gpsemulator.data.repository.RouteRepository

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_START_ROUTE = "ACTION_START_ROUTE"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_UPDATE_LOCATION = "ACTION_UPDATE_LOCATION"
        const val EXTRA_LAT = "EXTRA_LAT"
        const val EXTRA_LNG = "EXTRA_LNG"
        const val EXTRA_END_LAT = "EXTRA_END_LAT"
        const val EXTRA_END_LNG = "EXTRA_END_LNG"
        const val EXTRA_DURATION = "EXTRA_DURATION"
        const val NOTIFICATION_ID = 123
        const val CHANNEL_ID = "MockLocationChannel"
        const val PROVIDER = LocationManager.GPS_PROVIDER
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val lat = intent.getDoubleExtra(EXTRA_LAT, 0.0)
                val lng = intent.getDoubleExtra(EXTRA_LNG, 0.0)
                startMocking(lat, lng)
            }
            ACTION_START_ROUTE -> {
                val startLat = intent.getDoubleExtra(EXTRA_LAT, 0.0)
                val startLng = intent.getDoubleExtra(EXTRA_LNG, 0.0)
                val endLat = intent.getDoubleExtra(EXTRA_END_LAT, 0.0)
                val endLng = intent.getDoubleExtra(EXTRA_END_LNG, 0.0)
                val duration = intent.getIntExtra(EXTRA_DURATION, 60)
                startRouteSimulation(startLat, startLng, endLat, endLng, duration)
            }
            ACTION_UPDATE_LOCATION -> {
                val lat = intent.getDoubleExtra(EXTRA_LAT, 0.0)
                val lng = intent.getDoubleExtra(EXTRA_LNG, 0.0)
                updateLocation(lat, lng)
            }
            ACTION_STOP -> stopMocking()
        }
        return START_NOT_STICKY
    }

    private fun startRouteSimulation(startLat: Double, startLng: Double, endLat: Double, endLng: Double, duration: Int) {
        if (isMocking) stopMocking()
        
        try {
            startForeground(NOTIFICATION_ID, createNotification())
            setupTestProvider()
            
            isMocking = true
            repository.setMocking(true)
            
            serviceScope.launch {
                routeRepository.simulateRoute(startLat, startLng, endLat, endLng, duration).collect { (lat, lng) ->
                    if (!isMocking) cancel()
                    val loc = createLocation(lat, lng)
                    locationManager.setTestProviderLocation(PROVIDER, loc)
                    repository.setLocation(lat, lng)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopMocking()
        }
    }

    private fun startMocking(lat: Double, lng: Double) {
        if (isMocking) {
             updateLocation(lat, lng)
             return
        }
        
        try {
            startForeground(NOTIFICATION_ID, createNotification())
            setupTestProvider()
            
            isMocking = true
            repository.setMocking(true)
            updateLocation(lat, lng)
            
            serviceScope.launch {
                while (isMocking) {
                    currentLocation?.let {
                        it.time = System.currentTimeMillis()
                        it.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
                        locationManager.setTestProviderLocation(PROVIDER, it)
                    }
                    delay(1000) // Update every second
                }
            }
        } catch (e: SecurityException) {
            repository.setMocking(false)
            serviceScope.launch {
                repository.emitEvent(com.example.gpsemulator.data.repository.EmulationEvent.PermissionDenied)
            }
            stopMocking()
        } catch (e: Exception) {
            e.printStackTrace()
            stopMocking()
        }
    }

    private fun setupTestProvider() {
        locationManager.addTestProvider(
            PROVIDER,
            false, false, false, false, true, true, true,
            ProviderProperties.POWER_USAGE_LOW,
            ProviderProperties.ACCURACY_FINE
        )
        locationManager.setTestProviderEnabled(PROVIDER, true)
    }

    private fun updateLocation(lat: Double, lng: Double) {
        currentLocation = createLocation(lat, lng)
        repository.setLocation(lat, lng)
    }

    private fun stopMocking() {
        isMocking = false
        repository.setMocking(false)
        try {
            locationManager.removeTestProvider(PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createLocation(lat: Double, lng: Double): Location {
        return Location(PROVIDER).apply {
            latitude = lat
            longitude = lng
            altitude = 0.0
            accuracy = 1.0f
            time = System.currentTimeMillis()
            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("GPS Emulator Running")
            .setContentText("Mocking location...")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Try to use existing resource or fallback
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Mock Location Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        if (isMocking) {
            stopMocking()
        }
    }
}
