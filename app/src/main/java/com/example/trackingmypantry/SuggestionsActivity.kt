package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.db.entities.ItemSuggestion
import com.example.trackingmypantry.db.entities.PlaceSuggestion
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.adapters.IndexedArrayCallback
import com.example.trackingmypantry.lib.adapters.SuggestedItemsAdapter
import com.example.trackingmypantry.lib.adapters.SuggestedPlacesAdapter
import com.example.trackingmypantry.lib.viewmodels.DefaultAppViewModelFactory
import com.example.trackingmypantry.lib.viewmodels.SuggestionItemsViewModel
import com.example.trackingmypantry.lib.viewmodels.SuggestionPlacesViewModel

/**
 * @warning To start this activity, it is mandatory to put the extra `ITEMS_ELSE_PLACES_EXTRA`
 * in the intent, otherwise a NullPointerException will be raised.
 */
class SuggestionsActivity : AppCompatActivity() {
    companion object {
        const val ITEMS_ELSE_PLACES_EXTRA = "items_or_places"
    }

    private val itemSuggestionsToRemove = mutableListOf<ItemSuggestion>()
    private val placeSuggestionsToRemove = mutableListOf<PlaceSuggestion>()
    private val placeSuggestionsToMove = mutableListOf<PlaceSuggestion>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_suggestions)

        val recyclerView = this.findViewById<RecyclerView>(R.id.suggestionsRecView)
        val noElemsText = this.findViewById<TextView>(R.id.noElemsAdviseText)

        val itemsExtra = this.intent.extras!!.getBoolean(ITEMS_ELSE_PLACES_EXTRA)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SuggestedItemsAdapter(this.deleteItemSuggestion, arrayOf())

        if (itemsExtra) {
            val model: SuggestionItemsViewModel by viewModels {
                DefaultAppViewModelFactory(this.application)
            }

            model.getSuggestionItems().observe(this, Observer<List<ItemSuggestion>> {
                recyclerView.adapter = SuggestedItemsAdapter(this.deleteItemSuggestion, it.toTypedArray())
            })
        } else {
            val model: SuggestionPlacesViewModel by viewModels {
                DefaultAppViewModelFactory(this.application)
            }

            model.getSuggestionPlaces().observe(this, {
                recyclerView.adapter = SuggestedPlacesAdapter(
                    this.deletePlaceSuggestion,
                    this.movePlaceSuggestion,
                    it.toTypedArray()
                )
            })
        }
    }

    override fun onBackPressed() {
        if (this.itemSuggestionsToRemove.isNotEmpty()) {
            DbSingleton.getInstance(this).deleteItemSuggestions(
                *this.itemSuggestionsToRemove.toTypedArray()
            )
        }

        if (this.placeSuggestionsToRemove.isNotEmpty()) {
            DbSingleton.getInstance(this).deletePlaceSuggestions(
                *this.placeSuggestionsToRemove.toTypedArray()
            )
        }

        if (this.placeSuggestionsToMove.isNotEmpty()) {
            DbSingleton.getInstance(this).moveToPlaces(
                *this.placeSuggestionsToMove.toTypedArray()
            )
        }
        
        super.onBackPressed()
    }

    private val deleteItemSuggestion: IndexedArrayCallback<ItemSuggestion> = {
        this.itemSuggestionsToRemove.add(it.array[it.index])
    }

    private val deletePlaceSuggestion: IndexedArrayCallback<PlaceSuggestion> = {
        this.placeSuggestionsToRemove.add(it.array[it.index])
    }

    private val movePlaceSuggestion: IndexedArrayCallback<PlaceSuggestion> = {
        this.placeSuggestionsToMove.add(it.array[it.index])
    }
}