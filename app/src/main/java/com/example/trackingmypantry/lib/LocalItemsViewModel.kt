package com.example.trackingmypantry.lib

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trackingmypantry.db.entities.Item

class LocalItemsViewModel(app: Application): AndroidViewModel(app) {
    private val appContext = app.applicationContext

    private val localItems: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>().also {
            it.value = DbSingleton.getInstance(appContext).getAllItems()
        }
    }

    fun getLocalItems(): LiveData<List<Item>> {
        return localItems
    }
}