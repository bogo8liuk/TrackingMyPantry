package com.example.trackingmypantry.lib.data

import com.example.trackingmypantry.db.entities.ItemSuggestion
import com.example.trackingmypantry.db.entities.PlaceSuggestion

class Suggestion {
    var itemSuggestion: ItemSuggestion? = null
        private set
    var placeSuggestion: PlaceSuggestion? = null
        private set

    fun setItemSuggestion(suggestion: ItemSuggestion) {
        this.itemSuggestion = suggestion
        this.placeSuggestion = null
    }

    fun setPlaceSuggestion(suggestion: PlaceSuggestion) {
        this.placeSuggestion = suggestion
        this.itemSuggestion = null
    }

    fun isItem(): Boolean {
        return this.itemSuggestion != null
    }

    fun isPlace(): Boolean {
        return this.placeSuggestion != null
    }
}