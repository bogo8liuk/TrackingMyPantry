package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @warning To start this activity, it is mandatory to put the extra `ITEMS_ELSE_PLACES_EXTRA`
 * in the intent, otherwise a NullPointerException will be raised.
 */
class SuggestionsActivity : AppCompatActivity() {
    companion object {
        const val ITEMS_ELSE_PLACES_EXTRA = "items_or_places"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_suggestions)

        val recyclerView = this.findViewById<RecyclerView>(R.id.suggestionsRecView)
        val noElemsText = this.findViewById<TextView>(R.id.noElemsAdviseText)

        val itemsOrPlacesExtra = this.intent.extras!!.getBoolean(ITEMS_ELSE_PLACES_EXTRA)

        //TODO
    }
}