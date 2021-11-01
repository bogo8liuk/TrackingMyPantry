package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    val latitude: Double,
    val longitude: Double,
    val title: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)