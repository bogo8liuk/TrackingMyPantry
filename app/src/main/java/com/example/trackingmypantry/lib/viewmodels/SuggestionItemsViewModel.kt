package com.example.trackingmypantry.lib.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.trackingmypantry.db.entities.ItemSuggestion
import com.example.trackingmypantry.lib.DbSingleton

class SuggestionItemsViewModel(private val app: Application): AndroidViewModel(app) {
    private val suggestions = DbSingleton.getInstance(app.applicationContext).getAllItemSuggestions()

    fun getSuggestionItems(): LiveData<List<ItemSuggestion>> {
        return suggestions
    }
}