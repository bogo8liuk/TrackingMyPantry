package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.db.entities.Place

class AcceptSuggestionsActivity : AppCompatActivity() {
    companion object {
        const val BLUETOOTH_SOCKET_EXTRA = "btSocket"
        const val BLUETOOTH_THREAD_EXTRA = "btAccept"
    }

    private val itemSuggestions = mutableListOf<Item>()
    private val placeSuggestions = mutableListOf<Place>()

    private val readHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_accept_suggestions)


    }
}