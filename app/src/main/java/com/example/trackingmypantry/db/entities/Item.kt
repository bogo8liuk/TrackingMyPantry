package com.example.trackingmypantry.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

/* Abnormal entity, no present primary keys: it does not need a p.k. because it is simply
* the list of current items in the pantry, so it's not necessary to select one and only one row. */
@Entity(
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = arrayOf("barcode"),
        childColumns = arrayOf("barcode")
    )]
)
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val barcode: String,
    val purchase_date: Date,
    val expiration_date: Date?
)
