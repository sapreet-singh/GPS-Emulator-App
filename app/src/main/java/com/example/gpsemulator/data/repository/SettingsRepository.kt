package com.example.gpsemulator.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(@ApplicationContext private val context: Context) {
    
    companion object {
        val KEY_LAST_LAT = doublePreferencesKey("last_lat")
        val KEY_LAST_LNG = doublePreferencesKey("last_lng")
        val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
    }

    val lastLocation: Flow<Pair<Double, Double>?> = context.dataStore.data.map { prefs ->
        val lat = prefs[KEY_LAST_LAT]
        val lng = prefs[KEY_LAST_LNG]
        if (lat != null && lng != null) lat to lng else null
    }

    suspend fun saveLastLocation(lat: Double, lng: Double) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LAST_LAT] = lat
            prefs[KEY_LAST_LNG] = lng
        }
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_DARK_MODE] ?: false // Default to light/system
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DARK_MODE] = enabled
        }
    }
}
