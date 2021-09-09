package com.example.trackingmypantry.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trackingmypantry.db.daos.*
import com.example.trackingmypantry.db.entities.*

@Database(entities = [Collection::class, Item::class, Place::class, Suggestion::class, PlaceSuggestion::class], version = 2)
@TypeConverters(Converter::class)
abstract class Db: RoomDatabase() {
    abstract fun itemDao(): ItemDao
}