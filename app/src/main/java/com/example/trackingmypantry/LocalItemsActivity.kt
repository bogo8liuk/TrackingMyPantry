package com.example.trackingmypantry

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.db.entities.Collection
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.adapters.IndexedArrayCallback
import com.example.trackingmypantry.lib.adapters.LocalItemsAdapter
import com.example.trackingmypantry.lib.viewmodels.CollectionsViewModel
import com.example.trackingmypantry.lib.viewmodels.DefaultAppViewModelFactory
import com.example.trackingmypantry.lib.viewmodels.LocalItemsViewModel
import com.example.trackingmypantry.lib.viewmodels.LocalItemsViewModelFactory
import java.util.*

class LocalItemsActivity : AppCompatActivity() {
    companion object {
        const val COLLECTION_EXTRA = "collection"
    }

    private var collections: List<Collection>? = null
    private val itemsToClear = mutableListOf<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_local_items)

        val descriptionTextView: TextView = this.findViewById(R.id.localDescText)
        val recyclerView: RecyclerView = this.findViewById(R.id.localItemsRecView)

        recyclerView.adapter = LocalItemsAdapter(
            this.addQuantityToItem,
            this.removeQuantityToItem,
            this.removeItemFromCollection,
            this.setExpirationDate,
            true,
            arrayOf<Item>()
        )    // To avoid layout skipping
        recyclerView.layoutManager = LinearLayoutManager(this)

        val collection: Long = this.intent.extras!!.getLong(COLLECTION_EXTRA)

        val model: LocalItemsViewModel by viewModels {
            LocalItemsViewModelFactory(this.application, collection)
        }

        var adapter: LocalItemsAdapter
        if (collection < 0) {
            model.getLocalItems().observe(this, Observer<List<Item>> { it ->
                descriptionTextView.text = "Here are the products of your pantry"
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
                        this.setExpirationDate,
                        false,
                        items.toTypedArray()
                    )
                    recyclerView.adapter = adapter
                })
            })
        } else {
            model.getLocalCollectionItems().observe(this, Observer<List<Item>> {
                descriptionTextView.text = "Here are the products of your collection"
                adapter = LocalItemsAdapter(
                    this.addQuantityToItem,
                    this.removeQuantityToItem,
                    this.removeItemFromCollection,
                    this.setExpirationDate,
                    true,
                    it.toTypedArray()
                )
                recyclerView.adapter = adapter
            })
        }
    }

    override fun onBackPressed() {
        if (this.itemsToClear.isNotEmpty()) {
            DbSingleton.getInstance(this).removeMultipleItemsFromCollection(this.itemsToClear)
        }

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
        if (it.array[it.index].quantity > 0) {
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
        } else {
            Utils.toastShow(this, "No products to remove")
        }
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

    private val setExpirationDate: IndexedArrayCallback<Item> = {
        val datePicker = DatePicker(this)
        datePicker.minDate = Date().time

        AlertDialog.Builder(this)
            .setTitle("Expiration date")
            .setMessage("Set an expiration date, it will be saved")
            .setView(datePicker)
            .setNegativeButton(R.string.negativeCanc, null)
            .setPositiveButton(R.string.set, DialogInterface.OnClickListener { _, _ ->
                val expirationDate = Calendar.getInstance().also {
                    it.set(datePicker.year, datePicker.month, datePicker.dayOfMonth) }
                    .time

                DbSingleton.getInstance(this).changeExpirationDate(
                    it.array[it.index].id,
                    expirationDate
                )
            })
            .show()
    }
}