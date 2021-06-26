package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = arrayOf("barcode"),
        childColumns = arrayOf("barcode")
    )],
    primaryKeys = ["barcode", "place"]
)
data class Position(
    val barcode: String,
    val place: String
)