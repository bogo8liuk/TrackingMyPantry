package com.example.trackingmypantry.lib.data

data class Product(
    val name: String,
    val description: String,
    val barcode: String,
    val image: String
)

const val DEFAULT_BARCODE = "000000000000"
const val ERR_FIELD = "err"

fun special_err_product(statusCode: Int, err: String) =
    Product(
        ERR_FIELD,
        "$statusCode $err",
        ERR_FIELD,
        ERR_FIELD
    )