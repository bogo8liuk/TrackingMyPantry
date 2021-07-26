package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.LocalItemsAdapter
import com.example.trackingmypantry.lib.ReceivedItemsAdapter
import com.example.trackingmypantry.lib.data.Product
import com.example.trackingmypantry.lib.viewModel.LocalItemsViewModel
import com.example.trackingmypantry.lib.viewModel.LocalItemsViewModelFactory

class LocalItemsActivity : AppCompatActivity() {
    private lateinit var descriptionTextView: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_local_items)

        this.descriptionTextView = this.findViewById(R.id.localDescText)
        this.recyclerView = this.findViewById(R.id.localItemsRecView)

        this.recyclerView.adapter = LocalItemsAdapter(arrayOf<Item>())    // To avoid layout skipping
        this.recyclerView.layoutManager = LinearLayoutManager(this)

        val model: LocalItemsViewModel by viewModels {
            LocalItemsViewModelFactory(this.application)
        }

        model.getLocalItems().observe(this, Observer<List<Item>> {
            this.descriptionTextView.text = "Here are the products of your pantry"
            val adapter = LocalItemsAdapter(it.toTypedArray())
            this.recyclerView.adapter = adapter
        })
    }
}