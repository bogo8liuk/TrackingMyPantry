package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AcceptSuggestionsActivity : AppCompatActivity() {
    companion object {
        const val BLUETOOTH_SOCKET_EXTRA = "btSocket"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept_suggestions)
    }
}