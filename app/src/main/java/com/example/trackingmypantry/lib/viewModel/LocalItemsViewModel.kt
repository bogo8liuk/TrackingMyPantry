package com.example.trackingmypantry.lib.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton

class LocalItemsViewModel(app: Application): AndroidViewModel(app) {
    private val appContext = app.applicationContext

    private val localItems = DbSingleton.getInstance(appContext).getAllItems()

    fun getLocalItems(): LiveData<List<Item>> {
        return localItems
    }
}