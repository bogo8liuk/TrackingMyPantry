package com.example.trackingmypantry.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trackingmypantry.db.entities.Product

@Dao
interface ProductDao {
    @Query("select * from Product")
    fun getAllProducts(): List<Product>

    @Query("select * from Product where barcode like :barcode")
    fun getProductByBarcode(barcode: String): Product

    @Query("select * from Product where name like :name")
    fun getProductsByName(name: String): List<Product>

    @Insert
    fun insertProduct(product: Product)

    // Using query, because it is a specific update
    @Query("update product set image = :image where barcode like :barcode")
    fun insertImageForProduct(barcode: String, image: String)

    // Using query, because it is a specific update
    @Query("update product set rating = :rating where barcode like :barcode")
    fun insertRatingForProduct(barcode: String, rating: Int)
}