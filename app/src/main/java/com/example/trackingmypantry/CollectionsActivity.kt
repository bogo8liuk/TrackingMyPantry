package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import androidx.activity.viewModels
import com.example.trackingmypantry.db.entities.Collection
import com.example.trackingmypantry.lib.adapters.CollectionsAdapter
import com.example.trackingmypantry.lib.viewmodels.CollectionsViewModel
import com.example.trackingmypantry.lib.viewmodels.CollectionsViewModelFactory

class CollectionsActivity : AppCompatActivity() {
    private lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)

        this.gridView = findViewById(R.id.collectionsGridView)
        this.gridView.adapter = CollectionsAdapter(this, arrayOf<Collection>())

        val model: CollectionsViewModel by viewModels {
            CollectionsViewModelFactory(this.application)
        }
    }
}