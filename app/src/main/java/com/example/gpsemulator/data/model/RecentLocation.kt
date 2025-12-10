package com.example.gpsemulator.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_locations")
data class RecentLocation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)
