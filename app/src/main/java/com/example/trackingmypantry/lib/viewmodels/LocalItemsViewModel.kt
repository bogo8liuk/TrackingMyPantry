package com.example.trackingmypantry.lib.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton

class LocalItemsViewModel(app: Application, collection: Long?): AndroidViewModel(app) {
    private val appContext = app.applicationContext

    private val localItems = DbSingleton.getInstance(appContext).getAllItems()
    private val localCollectionItems = DbSingleton.getInstance(appContext).getItemsFromCollection(collection!!)

    fun getLocalItems(): LiveData<List<Item>> {
        return localItems
    }

    fun getLocalCollectionItems(): LiveData<List<Item>> {
        return localCollectionItems
    }
}