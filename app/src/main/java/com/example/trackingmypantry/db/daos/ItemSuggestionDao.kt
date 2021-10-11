package com.example.trackingmypantry.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trackingmypantry.db.entities.ItemSuggestion
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemSuggestionDao {
    @Query("select * from ItemSuggestion")
    fun getAllItemSuggestions(): Flow<List<ItemSuggestion>>

    @Insert
    suspend fun insertItemSuggestions(vararg suggestions: ItemSuggestion)

    @Delete
    suspend fun deleteItemSuggestion(vararg suggestions: ItemSuggestion)
}