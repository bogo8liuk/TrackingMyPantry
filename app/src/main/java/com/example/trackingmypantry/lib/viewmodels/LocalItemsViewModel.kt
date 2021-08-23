package com.example.trackingmypantry.lib.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton

class LocalItemsViewModel(app: Application): AndroidViewModel(app) {
    private val appContext = app.applicationContext

    private val localItems = DbSingleton.getInstance(appContext).getAllItems()

    fun getLocalItems(): LiveData<List<Item>> {
        return localItems
    }
}