package com.example.trackingmypantry

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.GridView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import com.example.trackingmypantry.db.entities.Collection
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.EvalMode
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.adapters.CollectionsAdapter
import com.example.trackingmypantry.lib.adapters.IndexedArrayCallback
import com.example.trackingmypantry.lib.viewmodels.CollectionsViewModel
import com.example.trackingmypantry.lib.viewmodels.DefaultAppViewModelFactory

class CollectionsActivity : AppCompatActivity() {
    private lateinit var gridView: GridView
    private lateinit var itemsButton: AppCompatButton
    private lateinit var createCollectionButton: AppCompatButton
    private lateinit var suggestionsButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)

        this.itemsButton = this.findViewById(R.id.localItemsButton)
        this.createCollectionButton = this.findViewById(R.id.createCollectionButton)
        this.suggestionsButton = this.findViewById(R.id.suggestionsButton)

        this.gridView = findViewById(R.id.collectionsGridView)
        this.gridView.adapter = CollectionsAdapter(this, arrayOf<Collection>())

        this.itemsButton.setOnClickListener {
            val intent = Intent(this, LocalItemsActivity::class.java)
            intent.putExtra("collection", -1L)   // No collection provided, just want to show all the items
            this.startActivity(intent)
        }

        this.createCollectionButton.setOnClickListener {
            val nameInput = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("Create a collection")
                .setView(nameInput)
                .setNegativeButton(R.string.negativeCanc, null)
                .setPositiveButton(R.string.create, DialogInterface.OnClickListener { _, _ ->
                    val name = nameInput.text.toString()

                    if (!Utils.stringPattern(EvalMode.WHITESPACE, name)) {
                        DbSingleton.getInstance(this).createCollections(Collection(name))
                    } else {
                        Utils.toastShow(this, "Invalid name for collection")
                    }
                })
                .show()
        }

        this.suggestionsButton.setOnClickListener {
            val actionButtons = RadioGroup(this)
            val itemsButton = RadioButton(this)
            val placesButton = RadioButton(this)

            itemsButton.text = "Add new locations"
            placesButton.text = "Remove your locations"

            actionButtons.addView(itemsButton)
            actionButtons.addView(placesButton)

            AlertDialog.Builder(this)
                .setTitle("Choose an action")
                .setMessage("Choose what to see")
                .setNegativeButton(R.string.negativeCanc, null)
                .setPositiveButton(R.string.positiveOk, DialogInterface.OnClickListener { _, _ ->
                    if (itemsButton.isChecked) {
                        val intent = Intent(this, SuggestionsActivity::class.java)
                        intent.putExtra(SuggestionsActivity.ITEMS_ELSE_PLACES_EXTRA, true)
                        this.startActivity(intent)
                    } else if (placesButton.isChecked) {
                        val intent = Intent(this, SuggestionsActivity::class.java)
                        intent.putExtra(SuggestionsActivity.ITEMS_ELSE_PLACES_EXTRA, false)
                        this.startActivity(intent)
                    }
                })
                .show()
        }

        val model: CollectionsViewModel by viewModels {
            DefaultAppViewModelFactory(this.application)
        }

        model.getCollections().observe(this, Observer<List<Collection>> {
            this.gridView.adapter = CollectionsAdapter(
                this,
                this.startLocalItemsActivity,
                it.toTypedArray()
            )
        })
    }

    private val startLocalItemsActivity: IndexedArrayCallback<Collection> = {
        val intent = Intent(this, LocalItemsActivity::class.java)
        intent.putExtra(LocalItemsActivity.COLLECTION_EXTRA, it.array[it.index].id)
        this.startActivity(intent)
    }
}