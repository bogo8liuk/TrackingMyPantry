package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey val barcode: String,
    val name: String,
    val description: String,
    val image: String?,     //TODO: format of the string
    val Rating: Int
)
