package com.example.trackingmypantry

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.db.entities.Place
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.adapters.LocationsToRemoveAdapter
import com.example.trackingmypantry.lib.viewmodels.DefaultAppViewModelFactory
import com.example.trackingmypantry.lib.viewmodels.LocationsViewModel

class RemoveLocationsActivity : AppCompatActivity() {
    private val locationsToRemove = mutableListOf<Place>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_locations)

        val descriptionText: TextView = this.findViewById(R.id.removeLocDescText)
        val recView: RecyclerView = this.findViewById(R.id.locationsRecView)
        val removeButton: AppCompatButton = this.findViewById(R.id.deleteLocationsButton)

        // Next two lines to avoid layout skipping
        recView.adapter = LocationsToRemoveAdapter(arrayOf<Place>(), { _ -> }, { _ -> })
        recView.layoutManager = LinearLayoutManager(this)

        val model: LocationsViewModel by viewModels {
            DefaultAppViewModelFactory(this.application)
        }

        model.getPlaces().observe(this, Observer<List<Place>> {
            if (it.isEmpty()) {
                descriptionText.text = "You did not saved any location yet"
            } else {
                descriptionText.text = "Choose the locations you wish to delete"
            }

            recView.adapter = LocationsToRemoveAdapter(
                it.toTypedArray(),
                { place ->
                    this.locationsToRemove.add(place)
                },
                { place ->
                    this.locationsToRemove.remove(place)
                }
            )
        })

        removeButton.setOnClickListener {
            if (this.locationsToRemove.isNotEmpty()) {
                DbSingleton.getInstance(this).deletePlaces(
                    places = this.locationsToRemove.toTypedArray()
                )

                Utils.toastShow(this, "Your locations will soon be removed")
            } else {
                Utils.toastShow(this, "No selected locations")
            }
        }
    }
}