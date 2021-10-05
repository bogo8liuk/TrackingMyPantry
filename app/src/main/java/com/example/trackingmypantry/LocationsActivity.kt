package com.example.trackingmypantry

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.db.entities.Place
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.EvalMode
import com.example.trackingmypantry.lib.PermissionEvaluer
import com.example.trackingmypantry.lib.Utils
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task

class LocationsActivity : AppCompatActivity(), OnMapReadyCallback {
    data class MarkerAndPosition (
        val marker: Marker?,
        val latitude: Double,
        val longitude: Double
    )

    private var markedLocations = mutableListOf<MarkerAndPosition>()
    private lateinit var map: GoogleMap

    private val DEFAULT_LOCATION_NAME = "My point of interest"
    private val LOCATION_REQUEST_FINE = 0

    // UI elements
    private lateinit var myPositionButton: AppCompatButton
    private lateinit var saveButton: AppCompatButton

    private fun addMarkedPosition(map: GoogleMap, title: String, location: LatLng) {
        this.markedLocations.add(
            MarkerAndPosition(
                map.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(title)
                ),
                location.latitude,
                location.longitude
            )
        )
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

                this.addMarkedPosition(map, title, LatLng(latitude, longitude))
            })
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_locations)

        this.myPositionButton = this.findViewById(R.id.myPositionButton)
        this.saveButton = this.findViewById(R.id.saveButton)

        this.saveButton.setOnClickListener {
            DbSingleton.getInstance(this).insertPlaces(
                places = this.markedLocations.map {
                    Place(it.latitude, it.longitude, it.marker?.title)
                }.toTypedArray()
            )

            Utils.toastShow(this, "Your changes will be saved")
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val locationClient = LocationServices.getFusedLocationProviderClient(this)
        val task: Task<Location> = locationClient.getCurrentLocation(
            LocationRequest.QUALITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        )

        Utils.toastShow(this, "It might take a few second")

        task.addOnSuccessListener { location ->
            this.showSetNameDialog(location.latitude, location.longitude, this.map)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.setOnMapClickListener { location ->
            this.showSetNameDialog(location.latitude, location.longitude, map)
        }

        this.myPositionButton.setOnClickListener {
            if (!PermissionEvaluer.got(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                PermissionEvaluer.request(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    LOCATION_REQUEST_FINE
                )
            } else {
                this.getCurrentLocation()
            }
        }
    }

    override fun onBackPressed() {
        if (this.markedLocations.isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Unsaved changes")
                .setMessage("There are unsaved changes, by going back you will" +
                        "lose them. Continuing?")
                .setNegativeButton(R.string.negative, null)
                .setPositiveButton(R.string.positive, DialogInterface.OnClickListener { _, _ ->
                    super.onBackPressed()
                })
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_REQUEST_FINE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.getCurrentLocation()
                } else {
                    Utils.toastShow(this, "Do not have permissions to use this functionality")
                }
            }
        }
    }
}