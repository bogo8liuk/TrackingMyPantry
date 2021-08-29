package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Item::class,
        parentColumns = arrayOf("item"),
        childColumns = arrayOf("id")
    )]
)
data class Grocery(
    val item: Long,
    val quantity: Int
)
