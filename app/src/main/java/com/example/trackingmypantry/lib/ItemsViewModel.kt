package com.example.trackingmypantry.lib

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trackingmypantry.db.entities.Item

class ItemsViewModel(app: Application): AndroidViewModel(app) {
    /* No memory leaks: there is only one Application instance when app is running. */
    private val appContext = app.applicationContext

    private val items: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>().also {
            DbSingleton.getInstance(appContext).getAllItems()   //TODO: turn it into a MutableLiveData
        }
    }

    fun getItems(): LiveData<List<Item>> {
        return items
    }
}