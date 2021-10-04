package com.example.trackingmypantry

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.trackingmypantry.lib.EvalMode
import com.example.trackingmypantry.lib.Utils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class LocationsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var currentMarker: Marker? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private val DEFAULT_LOCATION_NAME = "My point of interest"
    private lateinit var selectedLocation: String

    // UI elements
    private lateinit var locationText: TextView

    private fun changeLocationText(title: String) {
        this.selectedLocation = title + " at:\n" +
                this.latitude.toString() + ", " + this.longitude.toString()
        this.locationText.text = this.selectedLocation
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_locations)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        this.locationText = this.findViewById(R.id.locationText)
    }

    override fun onMapReady(map: GoogleMap) {
        map.setOnMapClickListener { location ->
            val nameEditText = EditText(this)
            var title = DEFAULT_LOCATION_NAME

            AlertDialog.Builder(this)
                .setTitle("Point of interest")
                .setMessage("Choose the name of your point of interest")
                .setView(nameEditText)
                .setNegativeButton(R.string.negativeCanc, null)
                .setPositiveButton(R.string.set, DialogInterface.OnClickListener { _, _ ->
                    if (!Utils.stringPattern(EvalMode.WHITESPACE, nameEditText.text.toString())) {
                        title = nameEditText.text.toString()
                    }

                    this.currentMarker?.remove()
                    this.currentMarker = map.addMarker(
                        MarkerOptions()
                            .position(LatLng(location.latitude, location.longitude))
                            .title(title)
                    )

                    this.latitude = location.latitude
                    this.longitude = location.longitude

                    this.changeLocationText(title)
                })
                .show()
        }
    }
}