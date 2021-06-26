package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Suggestion::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("suggestion")
    )],
    primaryKeys = ["suggestion", "place"]
)
data class PlaceSuggestion(
    val suggestion: Int,
    val place: String
)
