package com.example.trackingmypantry.lib

import android.content.Context
import androidx.room.Room
import com.example.trackingmypantry.db.Db
import com.example.trackingmypantry.db.entities.Item

class DbSingleton(context: Context) {
    // Boilerplate code equal to ReqQueueSingleton
    companion object {
        @Volatile
        private var dbInstance: DbSingleton? = null

        fun getInstance(context: Context) {
            dbInstance ?: synchronized(this) {
                dbInstance ?: DbSingleton(context).also {
                    dbInstance = it
                }
            }
        }
    }

    private val db = Room.databaseBuilder(
        context.applicationContext,
        Db::class.java,
        "pantry database"
    ).build()
    private val itemDao = db.itemDao()

    /* From here, wrapper functions */
    fun getAllItems(): List<Item> {
        return itemDao.getAllItems()
    }

    fun getItemsByBarcode(barcode: String): List<Item> {
        return itemDao.getItemsByBarcode(barcode)
    }

    fun getItemById(id: Int): Item {
        return itemDao.getItemById(id)
    }

    fun insertItems(vararg items: Item) {
        itemDao.insertItems(*items)
    }

    fun deleteItems(vararg items: Item) {
        itemDao.deleteItems(*items)
    }
}