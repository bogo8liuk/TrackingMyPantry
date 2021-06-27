package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Item::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("item")
    )],
    primaryKeys = ["item", "place"]
)
data class Place(
    val item: Long,
    val place: String
)