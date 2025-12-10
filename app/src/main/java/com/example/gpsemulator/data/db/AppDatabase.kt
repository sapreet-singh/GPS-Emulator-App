package com.example.gpsemulator.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gpsemulator.data.model.FavoriteLocation
import com.example.gpsemulator.data.model.RecentLocation

@Database(entities = [FavoriteLocation::class, RecentLocation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}
