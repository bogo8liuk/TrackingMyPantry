package com.example.trackingmypantry.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trackingmypantry.db.daos.ItemDao
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.db.entities.Place
import com.example.trackingmypantry.db.entities.PlaceSuggestion
import com.example.trackingmypantry.db.entities.Suggestion

@Database(entities = [Item::class, Place::class, Suggestion::class, PlaceSuggestion::class], version = 1)
abstract class db: RoomDatabase() {
    abstract fun itemDao(): ItemDao
}