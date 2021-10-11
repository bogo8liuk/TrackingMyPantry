package com.example.trackingmypantry.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trackingmypantry.db.entities.PlaceSuggestion
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceSuggestionDao {
    @Query("select * from PlaceSuggestion")
    fun getAllPlaceSuggestions(): Flow<List<PlaceSuggestion>>

    @Insert
    suspend fun insertPlaceSuggestions(vararg suggestions: PlaceSuggestion)

    @Delete
    suspend fun deletePlaceSuggestions(vararg suggestions: PlaceSuggestion)
}