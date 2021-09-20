package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Suggestion(
    val barcode: String,
    val name: String,
    val description: String,
    val image: String?, // TODO: see product table
    val user: String,    // The username that suggested the product
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)