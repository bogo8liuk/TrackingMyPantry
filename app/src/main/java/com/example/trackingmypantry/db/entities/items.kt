package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import java.util.*

@Entity
data class items(
    val barcode: String,
    val purchase_date: Date,
    val expiration_date: Date
)
