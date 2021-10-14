package com.example.trackingmypantry.lib.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.trackingmypantry.db.entities.PlaceSuggestion
import com.example.trackingmypantry.lib.DbSingleton

class SuggestionPlacesViewModel(app: Application): AndroidViewModel(app) {
    private val suggestions = DbSingleton.getInstance(app.applicationContext).getAllPlaceSuggestions()

    fun getSuggestionPlaces(): LiveData<List<PlaceSuggestion>> {
        return suggestions
    }
}