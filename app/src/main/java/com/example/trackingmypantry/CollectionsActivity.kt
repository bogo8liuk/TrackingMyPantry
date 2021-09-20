package com.example.trackingmypantry

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.GridView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import com.example.trackingmypantry.db.entities.Collection
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.EvalMode
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.adapters.CollectionsAdapter
import com.example.trackingmypantry.lib.viewmodels.CollectionsViewModel
import com.example.trackingmypantry.lib.viewmodels.CollectionsViewModelFactory

class CollectionsActivity : AppCompatActivity() {
    private lateinit var gridView: GridView
    private lateinit var itemsButton: AppCompatButton
    private lateinit var createCollectionButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)

        this.itemsButton = findViewById(R.id.localItemsButton)
        this.createCollectionButton = findViewById(R.id.createCollectionButton)

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
                .setNegativeButton(R.string.negative1, null)
                .setPositiveButton(R.string.create, DialogInterface.OnClickListener { _, _ ->
                    val name = nameInput.text.toString()

                    if (!Utils.stringPattern(EvalMode.WHITESPACE, name)) {
                        DbSingleton.getInstance(this).createCollection(Collection(name))
                    } else {
                        Utils.toastShow(this, "Invalid name for collection")
                    }
                })
                .show()
        }

        val model: CollectionsViewModel by viewModels {
            CollectionsViewModelFactory(this.application)
        }

        model.getCollections().observe(this, Observer<List<Collection>> {
            this.gridView.adapter = CollectionsAdapter(this, it.toTypedArray())
        })
    }
}