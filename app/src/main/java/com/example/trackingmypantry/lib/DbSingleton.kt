package com.example.trackingmypantry.lib

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.room.Room
import com.example.trackingmypantry.db.Db
import com.example.trackingmypantry.db.entities.Item
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DbSingleton(context: Context) {
    // Boilerplate code equal to ReqQueueSingleton
    companion object {
        @Volatile
        private var dbInstance: DbSingleton? = null

        fun getInstance(context: Context): DbSingleton {
            return dbInstance ?: synchronized(this) {
                dbInstance ?: DbSingleton(context).also {
                    dbInstance = it
                }
            }
        }
    }
    // End of boilerplate code

    private val db = Room.databaseBuilder(
        context.applicationContext,
        Db::class.java,
        "pantry-database"
    ).fallbackToDestructiveMigration()
    .build()
    private val itemDao = db.itemDao()

    /* From here, wrapper functions */
    fun getAllItems(): LiveData<List<Item>> {
        return itemDao.getAllItems().asLiveData()
    }

    fun getItemsByBarcode(barcode: String): LiveData<List<Item>> {
        return itemDao.getItemsByBarcode(barcode).asLiveData()
    }

    fun getItemById(id: Int): LiveData<Item> {
        return itemDao.getItemById(id).asLiveData()
    }

    fun getItemsFromCollection(collection: Long): LiveData<List<Item>> {
        return itemDao.getItemFromCollection(collection).asLiveData()
    }

    fun insertItems(vararg items: Item) {
        runBlocking {
            launch {
                itemDao.insertItems(*items)
            }
        }
    }

    fun changeQuantity(id: Long, adding: Int) {
        runBlocking {
            launch {
                itemDao.changeQuantity(id, adding)
            }
        }
    }
}