package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val barcode: String,
    val name: String,
    val description: String,
    val image: String?,
    val rating: Int,
    val purchase_date: Date,
    val expiration_date: Date?
)
