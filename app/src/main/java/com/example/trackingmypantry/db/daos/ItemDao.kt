package com.example.trackingmypantry.db.daos

import androidx.room.*
import com.example.trackingmypantry.db.entities.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("select * from item")
    fun getAllItems(): Flow<List<Item>>

    @Query("select * from item where barcode like :barcode")
    fun getItemsByBarcode(barcode: String): Flow<List<Item>>

    @Query("select * from Item where id like :id")
    fun getItemById(id: Int): Flow<Item> //TODO: evaluate to change also normal query functions

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItems(vararg items: Item)
}