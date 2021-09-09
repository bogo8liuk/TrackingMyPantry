package com.example.trackingmypantry.lib.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.trackingmypantry.db.entities.Collection
import com.example.trackingmypantry.lib.DbSingleton

class CollectionsViewModel(app: Application): AndroidViewModel(app) {
    private val appContext = app.applicationContext

    private val collections = DbSingleton.getInstance(appContext).getAllCollections()

    fun getCollections(): LiveData<List<Collection>> {
        return collections
    }
}