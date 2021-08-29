package com.example.trackingmypantry.db.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.trackingmypantry.db.entities.Grocery
import com.example.trackingmypantry.db.entities.Item

@Dao
interface GroceryDao {
    @Query("select * from Grocery as G, Item as I where G.item = I.id")
    suspend fun getAllItems(): List<Item>

    @Update
    suspend fun changeQuantity(vararg items: Grocery)
}