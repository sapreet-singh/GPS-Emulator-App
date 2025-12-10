package com.example.gpsemulator.domain.repository

import com.example.gpsemulator.data.model.FavoriteLocation
import com.example.gpsemulator.data.model.RecentLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getAllFavorites(): Flow<List<FavoriteLocation>>
    suspend fun insertFavorite(location: FavoriteLocation)
    suspend fun deleteFavorite(location: FavoriteLocation)
    
    fun getRecentLocations(): Flow<List<RecentLocation>>
    suspend fun insertRecent(location: RecentLocation)
    suspend fun clearRecents()
}
