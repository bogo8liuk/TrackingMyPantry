package com.example.trackingmypantry.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trackingmypantry.db.entities.Item

@Dao
interface ItemDao {
    @Query("select * from item")
    fun getAllItems(): List<Item>

    @Query("select * from item where barcode like :barcode")
    fun getItemsByBarcode(barcode: String): List<Item>

    @Query("select * from Item where id like :id")
    fun getItemById(id: Int): Item

    @Insert
    fun insertItems(vararg items: Item)

    @Delete
    fun deleteItems(vararg items: Item)
}