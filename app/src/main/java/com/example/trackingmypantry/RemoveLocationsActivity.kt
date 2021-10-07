package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView

class RemoveLocationsActivity : AppCompatActivity() {
    private lateinit var descriptionText: TextView
    private lateinit var recView: RecyclerView
    private lateinit var removeButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_locations)
    }
}