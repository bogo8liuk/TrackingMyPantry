package com.example.trackingmypantry.lib.data

data class Product(
    val name: String,
    val description: String,
    val barcode: String,
    val image: String,
    val id: String
)

const val DEFAULT_BARCODE = "0000000000000"
const val ERR_FIELD = "err"

fun specialErrProduct(statusCode: Int, err: String) =
    Product(
        ERR_FIELD,
        "$statusCode $err",
        ERR_FIELD,
        ERR_FIELD,
        ERR_FIELD
    )

fun isErrProduct(product: Product) = product.barcode == ERR_FIELD