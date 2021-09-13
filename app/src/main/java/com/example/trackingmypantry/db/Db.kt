package com.example.trackingmypantry.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trackingmypantry.db.daos.*
import com.example.trackingmypantry.db.entities.*
import com.example.trackingmypantry.db.entities.Collection

@Database(entities = [Collection::class, Item::class, Place::class, Suggestion::class, PlaceSuggestion::class], version = 3)
@TypeConverters(Converter::class)
abstract class Db: RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun collectionDao(): CollectionDao
}