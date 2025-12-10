package com.example.gpsemulator

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GpsEmulatorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        try {
            val appInfo = checkNotNull(packageManager.getApplicationInfo(packageName, android.content.pm.PackageManager.GET_META_DATA))
            val apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY")
            if (!apiKey.isNullOrEmpty()) {
                com.google.android.libraries.places.api.Places.initialize(applicationContext, apiKey)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
