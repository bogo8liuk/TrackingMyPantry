package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.db.entities.Collection
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.adapters.IndexedArrayCallback
import com.example.trackingmypantry.lib.adapters.LocalItemsAdapter
import com.example.trackingmypantry.lib.viewmodels.*

class LocalItemsActivity : AppCompatActivity() {
    companion object {
        const val COLLECTION_EXTRA = "collection"
    }

    private var collections: List<Collection>? = null
    private val itemsToClear = mutableListOf<Long>()

    private lateinit var descriptionTextView: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_local_items)

        this.descriptionTextView = this.findViewById(R.id.localDescText)
        this.recyclerView = this.findViewById(R.id.localItemsRecView)

        this.recyclerView.adapter = LocalItemsAdapter(
            this.addQuantityToItem,
            this.removeQuantityToItem,
            this.removeItemFromCollection,
            true,
            arrayOf<Item>()
        )    // To avoid layout skipping
        this.recyclerView.layoutManager = LinearLayoutManager(this)

        val collection: Long = this.intent.extras!!.getLong(COLLECTION_EXTRA)

        val model: LocalItemsViewModel by viewModels {
            LocalItemsViewModelFactory(this.application, collection)
        }

        var adapter: LocalItemsAdapter
        if (collection < 0) {
            model.getLocalItems().observe(this, Observer<List<Item>> { it ->
                this.descriptionTextView.text = "Here are the products of your pantry"
                val items = it

                val collectionModel: CollectionsViewModel by viewModels {
                    DefaultAppViewModelFactory(this.application)
                }

                collectionModel.getCollections().observe(this, Observer<List<Collection>> {
                    this.collections = it
                    adapter = LocalItemsAdapter(
                        this.addQuantityToItem,
                        this.removeQuantityToItem,
                        this.addItemToCollection,
                        false,
                        items.toTypedArray()
                    )
                    this.recyclerView.adapter = adapter
                })
            })
        } else {
            model.getLocalCollectionItems().observe(this, Observer<List<Item>> {
                this.descriptionTextView.text = "Here are the products of your collection"
                adapter = LocalItemsAdapter(
                    this.addQuantityToItem,
                    this.removeQuantityToItem,
                    this.removeItemFromCollection,
                    true,
                    it.toTypedArray()
                )
                this.recyclerView.adapter = adapter
            })
        }
    }

    override fun onBackPressed() {
        DbSingleton.getInstance(this).removeMultipleItemsFromCollection(this.itemsToClear)

        super.onBackPressed()
    }

    /**
     * It returns the collection id, given a position of a collection. It is assured not
     * to have conflicts between names because they are unique.
     */
    private fun getCollectionFromName(position: Int, collections: List<Collection>): Long? {
        for ((c, collection) in collections.withIndex()) {
            if (c == position) {
                return collection.id
            }
        }

        Log.e("Unreachable", "Value out of range while iterating list of collections")
        return null
    }

    private val addQuantityToItem: IndexedArrayCallback<Item> = {
        val quantityPicker = NumberPicker(this)
        quantityPicker.minValue = 1
        quantityPicker.maxValue = 50    // arbitrary
        AlertDialog.Builder(this)
            .setTitle("Buy")
            .setMessage("Choose the quantity of this product you want to buy")
            .setView(quantityPicker)
            .setNegativeButton(R.string.negativeCanc, null)
            .setPositiveButton(R.string.buy) { _, _ ->
                DbSingleton.getInstance(this).changeItemQuantity(
                    it.array[it.index].id,
                    quantityPicker.value
                )
            }
            .show()
    }

    private val removeQuantityToItem: IndexedArrayCallback<Item> = {
        val quantityPicker = NumberPicker(this)
        quantityPicker.minValue = 1
        quantityPicker.maxValue = it.array[it.index].quantity
        AlertDialog.Builder(this)
            .setTitle("Remove")
            .setMessage("Choose the quantity of this product you want to remove")
            .setView(quantityPicker)
            .setNegativeButton(R.string.negativeCanc, null)
            .setPositiveButton(R.string.remove) { _, _ ->
                DbSingleton.getInstance(this).changeItemQuantity(
                    it.array[it.index].id,
                    -quantityPicker.value
                )
            }
            .show()
    }

    private val addItemToCollection: IndexedArrayCallback<Item> = {
        if (this.collections!!.isEmpty()) {
            Utils.toastShow(this, "Create a collection before")
        } else {
            val values = mutableListOf<String>()
            for (collection in this.collections!!) {
                values.add(collection.name)
            }

            val collectionPicker = NumberPicker(this)
            collectionPicker.minValue = 0
            collectionPicker.maxValue = values.size - 1
            collectionPicker.displayedValues = values.toTypedArray()

            AlertDialog.Builder(this)
                .setTitle("Collections")
                .setMessage("Choose a collection")
                .setView(collectionPicker)
                .setNegativeButton(R.string.negativeCanc, null)    // do nothing
                .setPositiveButton(R.string.choose) { _, _ ->
                    DbSingleton.getInstance(this).insertItemIntoCollection(
                        it.array[it.index].id,
                        this.getCollectionFromName(collectionPicker.value, this.collections!!)!!
                    )
                }
                .show()
        }
    }

    private val removeItemFromCollection: IndexedArrayCallback<Item> = {
        this.itemsToClear.add(it.array[it.index].id)
        Utils.toastShow(this, "Your item will be updated soon")
    }
}