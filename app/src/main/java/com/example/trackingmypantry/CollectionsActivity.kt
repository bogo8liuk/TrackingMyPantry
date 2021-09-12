package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView

class CollectionsActivity : AppCompatActivity() {
    private lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)

        this.gridView = findViewById(R.id.collectionsGridView)
    }
}