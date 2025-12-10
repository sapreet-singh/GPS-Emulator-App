package com.example.gpsemulator.data.repository

import com.example.gpsemulator.data.db.LocationDao
import com.example.gpsemulator.data.model.FavoriteLocation
import com.example.gpsemulator.data.model.RecentLocation
import com.example.gpsemulator.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDao: LocationDao
) : LocationRepository {
    override fun getAllFavorites(): Flow<List<FavoriteLocation>> = locationDao.getAllFavorites()

    override suspend fun insertFavorite(location: FavoriteLocation) = locationDao.insertFavorite(location)

    override suspend fun deleteFavorite(location: FavoriteLocation) = locationDao.deleteFavorite(location)

    override fun getRecentLocations(): Flow<List<RecentLocation>> = locationDao.getRecentLocations()

    override suspend fun insertRecent(location: RecentLocation) = locationDao.insertRecent(location)

    override suspend fun clearRecents() = locationDao.clearRecents()
}
