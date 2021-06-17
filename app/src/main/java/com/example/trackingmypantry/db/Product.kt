package com.example.trackingmypantry.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey val barcode: String
)
