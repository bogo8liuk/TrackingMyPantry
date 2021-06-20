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

    @Query("update product set image = :image where barcode like :barcode")
    fun insertImageForProduct(barcode: String, image: String)

    @Query("update product set personal_comment = :comment where barcode like :barcode")
    fun insertCommentForProduct(barcode: String, comment: String)
}