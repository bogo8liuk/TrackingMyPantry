package com.example.trackingmypantry.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.db.entities.joinsResults.ProductItem

@Dao
interface ItemDao {
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
    fun insertItems(vararg items: Item)

    @Delete
    fun deleteItems(vararg items: Item)
}