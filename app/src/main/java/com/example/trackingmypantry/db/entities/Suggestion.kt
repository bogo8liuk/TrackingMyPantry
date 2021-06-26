package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = arrayOf("barcode"),
        childColumns = arrayOf("barcode")
    )]
)
data class Suggestion(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val barcode: String,
    val image: String?, // TODO: see product table
    val rating: Int,
    val user: String    // The username that suggested the product
)