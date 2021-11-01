package com.example.trackingmypantry.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trackingmypantry.db.entities.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("select * from Place")
    fun getAllPlaces(): Flow<List<Place>>

    @Insert
    suspend fun insertPlaces(vararg places: Place)

    @Delete
    suspend fun deletePlaces(vararg places: Place)
}