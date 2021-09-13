package com.example.trackingmypantry.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trackingmypantry.db.entities.Collection
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Query("select * from Collection")
    fun getAllCollections(): Flow<List<Collection>>

    @Insert
    suspend fun insertCollection(vararg collections: Collection)
}