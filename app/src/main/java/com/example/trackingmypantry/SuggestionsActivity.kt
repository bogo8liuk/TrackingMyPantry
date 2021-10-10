package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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
        setContentView(R.layout.activity_suggestions)
    }
}