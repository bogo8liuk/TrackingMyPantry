package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = arrayOf("barcode"),
        childColumns = arrayOf("barcode")
    )
])
data class Suggestions(
    val barcode: String,
    val image: String?, // TODO: see product table
    val place: String?,
    val comment: String?,
    val user: String    // The username that suggested the product
)
