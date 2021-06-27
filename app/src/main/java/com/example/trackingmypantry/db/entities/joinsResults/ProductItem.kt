package com.example.trackingmypantry.db.entities.joinsResults

import java.util.*

/* "Return value" of the join between product and items */
data class ProductItem(
    val barcode: String,
    val name: String,
    val purchase_date: Date,
    val expiration_date: Date?
)