package com.example.trackingmypantry.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trackingmypantry.db.entities.Item
import java.util.*

@Dao
interface ItemsDao {
    /* "Return value" of the join between product and items */
    data class ProductItem(
        val barcode: String,
        val name: String,
        val purchase_date: Date,
        val expiration_date: Date?
    )

    @Query("select P.barcode, P.name, I.purchase_date, I.expiration_date " +
            "from items as I, product as P where I.barcode = P.barcode")
    fun getCurrentItems(): List<ProductItem>

    @Query("select P.barcode, P.name, I.purchase_date, I.expiration_date " +
            "from items as I, product as P where I.barcode = P.barcode and" +
            "P.barcode like :barcode")
    fun getCurrentItemsByBarcode(barcode: String): List<ProductItem>

    @Query("select * from Item where id like :id")
    fun getItemById(id: Int): Item

    @Insert
    fun insertItem(item: Item)

    //TODO: finish
}