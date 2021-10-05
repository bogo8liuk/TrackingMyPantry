package com.example.trackingmypantry

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.db.entities.Collection
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.adapters.LocalItemsAdapter
import com.example.trackingmypantry.lib.viewmodels.CollectionsViewModel
import com.example.trackingmypantry.lib.viewmodels.CollectionsViewModelFactory
import com.example.trackingmypantry.lib.viewmodels.LocalItemsViewModel
import com.example.trackingmypantry.lib.viewmodels.LocalItemsViewModelFactory

class LocalItemsActivity : AppCompatActivity() {
    private lateinit var descriptionTextView: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_local_items)

        this.descriptionTextView = this.findViewById(R.id.localDescText)
        this.recyclerView = this.findViewById(R.id.localItemsRecView)

        this.recyclerView.adapter = LocalItemsAdapter(arrayOf<Item>(), null)    // To avoid layout skipping
        this.recyclerView.layoutManager = LinearLayoutManager(this)

        val collection: Long = this.intent.extras!!.getLong("collection")

        val model: LocalItemsViewModel by viewModels {
            LocalItemsViewModelFactory(this.application, collection)
        }

        var adapter: LocalItemsAdapter
        if (collection < 0) {
            model.getLocalItems().observe(this, Observer<List<Item>> { it ->
                this.descriptionTextView.text = "Here are the products of your pantry"
                val items = it

                val collectionModel: CollectionsViewModel by viewModels {
                    CollectionsViewModelFactory(this.application)
                }

                collectionModel.getCollections().observe(this, Observer<List<Collection>> {
                    adapter = LocalItemsAdapter(items.toTypedArray(), it)
                    this.recyclerView.adapter = adapter
                })
            })
        } else {
            model.getLocalCollectionItems().observe(this, Observer<List<Item>> {
                this.descriptionTextView.text = "Here are the products of your collection"
                adapter = LocalItemsAdapter(it.toTypedArray(), null)
                this.recyclerView.adapter = adapter
            })
        }
    }
}