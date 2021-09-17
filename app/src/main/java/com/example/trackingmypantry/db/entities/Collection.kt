package com.example.trackingmypantry.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = arrayOf(Index(value = ["name"], unique = true))
)
data class Collection(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "name")val name: String  // Unique
)
