package com.example.trackingmypantry.lib.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.trackingmypantry.db.entities.Place
import com.example.trackingmypantry.lib.DbSingleton

class LocationsViewModel(app: Application): AndroidViewModel(app) {
    private val appContext = app.applicationContext

    private val locations = DbSingleton.getInstance(appContext).getAllPlaces()

    fun getPlaces(): LiveData<List<Place>> {
        return locations
    }
}