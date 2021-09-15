package com.example.trackingmypantry.lib.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton

/**
 * The constructor of this viewmodel takes a collection id; if `getLocalItems()` is called,
 * the collection id is useless and then ignored.
 */
class LocalItemsViewModel(app: Application, collection: Long): AndroidViewModel(app) {
    private val appContext = app.applicationContext

    private val localItems = DbSingleton.getInstance(appContext).getAllItems()
    private val localCollectionItems = DbSingleton.getInstance(appContext).getItemsFromCollection(collection)

    fun getLocalItems(): LiveData<List<Item>> {
        return localItems
    }

    fun getLocalCollectionItems(): LiveData<List<Item>> {
        return localCollectionItems
    }
}