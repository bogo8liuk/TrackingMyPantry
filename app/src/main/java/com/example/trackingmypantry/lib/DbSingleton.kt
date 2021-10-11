package com.example.trackingmypantry.lib

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.room.Room
import com.example.trackingmypantry.db.Db
import com.example.trackingmypantry.db.entities.*
import com.example.trackingmypantry.db.entities.Collection
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.suspendCoroutine

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
    private val collectionDao = db.collectionDao()
    private val placeDao = db.placeDao()
    private val itemSuggestionDao = db.itemSuggestionDao()
    private val placeSuggestionDao = db.placeSuggestionDao()

    /* From here, wrapper functions */
    fun getAllItems(): LiveData<List<Item>> {
        return itemDao.getAllItems().asLiveData()
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

    fun changeItemQuantity(id: Long, adding: Int) {
        runBlocking {
            launch {
                itemDao.changeItemQuantity(id, adding)
            }
        }
    }

    fun removeItemFromCollection(id: Long) {
        runBlocking {
            launch {
                itemDao.removeItemFromCollection(id)
            }
        }
    }

    fun insertItemIntoCollection(id: Long, collection: Long) {
        runBlocking {
            launch {
                itemDao.insertItemIntoCollection(id, collection)
            }
        }
    }

    fun getAllCollections(): LiveData<List<Collection>> {
        return collectionDao.getAllCollections().asLiveData()
    }

    fun createCollections(vararg collections: Collection) {
        runBlocking {
            launch {
                collectionDao.insertCollection(*collections)
            }
        }
    }

    fun getAllPlaces(): LiveData<List<Place>> {
        return placeDao.getAllPlaces().asLiveData()
    }

    fun insertPlaces(vararg places: Place) {
        runBlocking {
            launch {
                placeDao.insertPlaces(*places)
            }
        }
    }

    fun deletePlaces(vararg places: Place) {
        runBlocking {
            launch {
                placeDao.deletePlaces(*places)
            }
        }
    }

    fun getAllItemSuggestions(): LiveData<List<ItemSuggestion>> {
        return itemSuggestionDao.getAllItemSuggestions().asLiveData()
    }

    fun insertItemSuggestions(vararg suggestions: ItemSuggestion) {
        runBlocking {
            launch {
                itemSuggestionDao.insertItemSuggestions(*suggestions)
            }
        }
    }

    fun deleteItemSuggestions(vararg suggestions: ItemSuggestion) {
        runBlocking {
            launch {
                itemSuggestionDao.deleteItemSuggestion(*suggestions)
            }
        }
    }

    fun getAllPlaceSuggestions(): LiveData<List<PlaceSuggestion>> {
        return placeSuggestionDao.getAllPlaceSuggestions().asLiveData()
    }

    fun insertPlaceSuggestions(vararg suggestions: PlaceSuggestion) {
        runBlocking {
            launch {
                placeSuggestionDao.insertPlaceSuggestions(*suggestions)
            }
        }
    }

    fun deletePlaceSuggestions(vararg suggestions: PlaceSuggestion) {
        runBlocking {
            launch {
                placeSuggestionDao.deletePlaceSuggestions(*suggestions)
            }
        }
    }

    fun moveToPlaces(vararg suggestions: PlaceSuggestion) {
        runBlocking {
            launch {
                db.runInTransaction(Runnable {
                    this.launch {
                        val places = Array<Place>(suggestions.size) { i ->
                            Place(suggestions[i].latitude, suggestions[i].longitude, suggestions[i].title)
                        }

                        placeDao.insertPlaces(*places)
                        placeSuggestionDao.deletePlaceSuggestions(*suggestions)
                    }
                })
            }
        }
    }
}