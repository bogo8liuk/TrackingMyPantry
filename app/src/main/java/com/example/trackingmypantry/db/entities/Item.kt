package com.example.trackingmypantry.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = Collection::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("collection")
    )]
)
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val barcode: String,
    val name: String,
    val description: String,
    val image: String?,
    val purchase_date: Date,
    val expiration_date: Date?,
    val collection: Long?,
    @ColumnInfo(defaultValue = "1") val quantity: Int
)
