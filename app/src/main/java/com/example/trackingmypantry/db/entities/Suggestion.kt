package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Suggestion(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val barcode: String,
    val name: String,
    val description: String,
    val image: String?, // TODO: see product table
    val rating: Int,
    val user: String    // The username that suggested the product
)