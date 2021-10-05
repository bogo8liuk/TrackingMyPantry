package com.example.trackingmypantry.db.daos

import androidx.room.*
import com.example.trackingmypantry.db.entities.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("select * from Item")
    fun getAllItems(): Flow<List<Item>>

    @Query("select * from Item where barcode like :barcode")
    fun getItemsByBarcode(barcode: String): Flow<List<Item>>

    @Query("select * from Item where id like :id")
    fun getItemById(id: Int): Flow<Item>

    @Query("select * from Item where collection like :collection")
    fun getItemFromCollection(collection: Long): Flow<List<Item>>

    @Insert
    suspend fun insertItems(vararg items: Item)

    // Update
    @Query("update Item set quantity=quantity+:adding where id like :id")
    suspend fun changeItemQuantity(id: Long, adding: Int)

    // Update
    @Query("update Item set collection=null where id like :id")
    suspend fun removeItemFromCollection(id: Long)

    // Update
    @Query("update Item set collection=:collectionId where id like :itemId")
    suspend fun insertItemIntoCollection(itemId: Long, collectionId: Long)
}