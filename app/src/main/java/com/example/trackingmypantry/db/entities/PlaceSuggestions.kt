package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Suggestions::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("suggestion")
    )],
    primaryKeys = ["suggestion", "place"]
)
data class PlaceSuggestions(
    val suggestion: Int,
    val place: String
)
