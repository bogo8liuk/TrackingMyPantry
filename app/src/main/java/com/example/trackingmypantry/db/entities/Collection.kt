package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Collection(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String
)
