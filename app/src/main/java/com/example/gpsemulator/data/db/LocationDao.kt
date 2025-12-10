package com.example.gpsemulator.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gpsemulator.data.model.FavoriteLocation
import com.example.gpsemulator.data.model.RecentLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    // Favorites
    @Query("SELECT * FROM favorite_locations ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(location: FavoriteLocation)

    @Delete
    suspend fun deleteFavorite(location: FavoriteLocation)

    // Recents
    @Query("SELECT * FROM recent_locations ORDER BY timestamp DESC LIMIT 20")
    fun getRecentLocations(): Flow<List<RecentLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecent(location: RecentLocation)
    
    @Query("DELETE FROM recent_locations")
    suspend fun clearRecents()
}
