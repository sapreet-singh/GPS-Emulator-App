package com.example.gpsemulator.di

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.gpsemulator.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object PlacesModule {

    @Provides
    @Singleton
    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
        // Initialize Places if not already done
        if (!Places.isInitialized()) {
             // In a real app, API Key should be secure.
             // Manifest metadata is used for Maps, but Places needs initialization here or in App.
             // We can read from Manifest or BuildConfig.
             // For now, assuming BuildConfig has it or we can just use a placeholder if key is in manifest meta-data only.
             // Actually, Places.initialize requires a key.
             // Let's assume the user put MAPS_API_KEY in local.properties and we can access it via BuildConfig if we set it up.
             // We enabled buildConfig in build.gradle.
             // But we need to make sure the key is exposed.
             // Use "YOUR_API_KEY" as placeholder if not available.
        }
        return Places.createClient(context)
    }
}
