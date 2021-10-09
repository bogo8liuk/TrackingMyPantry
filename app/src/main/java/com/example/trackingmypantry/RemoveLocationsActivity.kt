package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.db.entities.Place
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.adapters.LocationsToRemoveAdapter
import com.example.trackingmypantry.lib.viewmodels.LocationsToRemoveViewModel
import com.example.trackingmypantry.lib.viewmodels.LocationsToRemoveViewModelFactory

class RemoveLocationsActivity : AppCompatActivity() {
    private lateinit var descriptionText: TextView
    private lateinit var recView: RecyclerView
    private lateinit var removeButton: AppCompatButton

    private val locationsToRemove = mutableListOf<Place>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_locations)

        this.descriptionText = this.findViewById(R.id.removeLocDescText)
        this.recView = this.findViewById(R.id.locationsRecView)
        this.removeButton = this.findViewById(R.id.deleteLocationsButton)

        // Next two lines to avoid layout skipping
        this.recView.adapter = LocationsToRemoveAdapter(arrayOf<Place>(), { _ -> }, { _ -> })
        this.recView.layoutManager = LinearLayoutManager(this)

        val model: LocationsToRemoveViewModel by viewModels {
            LocationsToRemoveViewModelFactory(this.application)
        }

        model.getPlaces().observe(this, Observer<List<Place>> {
            if (it.isEmpty()) {
                this.descriptionText.text = "You did not saved any location yet"
            } else {
                this.descriptionText.text = "Choose the locations you wish to delete"
            }

            this.recView.adapter = LocationsToRemoveAdapter(
                it.toTypedArray(),
                { place ->
                    this.locationsToRemove.add(place)
                },
                { place ->
                    this.locationsToRemove.remove(place)
                }
            )
        })

        this.removeButton.setOnClickListener {
            if (this.locationsToRemove.isNotEmpty()) {
                DbSingleton.getInstance(this).deletePlaces(
                    places = this.locationsToRemove.toTypedArray()
                )

                Utils.toastShow(this, "Your locations will soon be removed")
            } else {
                Utils.toastShow(this, "No selecte locations")
            }

            this.finish()
        }
    }
}