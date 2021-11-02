package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemSuggestion(
    val barcode: String,
    val name: String,
    val description: String,
    val user: String,    // The username that suggested the product
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)