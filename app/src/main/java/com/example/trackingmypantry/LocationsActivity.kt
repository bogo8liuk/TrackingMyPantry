package com.example.trackingmypantry

import android.content.DialogInterface
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.lib.EvalMode
import com.example.trackingmypantry.lib.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource

class LocationsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var currentMarker: Marker? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private lateinit var map: GoogleMap
    private val DEFAULT_LOCATION_NAME = "My point of interest"
    private lateinit var selectedLocation: String

    // UI elements
    private lateinit var locationText: TextView
    private lateinit var myPositionButton: AppCompatButton
    private lateinit var saveButton: AppCompatButton

    private fun updateCurrentMarker(map: GoogleMap, title: String, location: LatLng) {
        this.currentMarker?.remove()
        this.currentMarker = map.addMarker(
            MarkerOptions()
                .position(location)
                .title(title)
        )
    }

    private fun changeLocationText(title: String) {
        this.selectedLocation = title
        this.locationText.text = this.selectedLocation
    }

    private fun showSetNameDialog(latitude: Double, longitude: Double, map: GoogleMap) {
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

                val location = LatLng(latitude, longitude)

                this.updateCurrentMarker(map, title, location)

                this.latitude = location.latitude
                this.longitude = location.longitude

                this.changeLocationText(title)
            })
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_locations)

        this.locationText = this.findViewById(R.id.locationText)
        this.myPositionButton = this.findViewById(R.id.myPositionButton)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.setOnMapClickListener { location ->
            this.showSetNameDialog(location.latitude, location.longitude, map)
        }

        this.myPositionButton.setOnClickListener {
            val locationClient = LocationServices.getFusedLocationProviderClient(this)
            val task = locationClient.getCurrentLocation(
                LocationRequest.QUALITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            )

            task.addOnSuccessListener { location ->
                this.showSetNameDialog(location.latitude, location.longitude, this.map)
            }
        }
    }
}