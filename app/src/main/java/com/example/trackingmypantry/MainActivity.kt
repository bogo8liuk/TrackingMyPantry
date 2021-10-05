package com.example.trackingmypantry

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import com.example.trackingmypantry.lib.EvalMode
import com.example.trackingmypantry.lib.PermissionEvaluer
import com.example.trackingmypantry.lib.credentials.TokenHandler
import com.example.trackingmypantry.lib.credentials.TokenType
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.ResultCode
import com.example.trackingmypantry.lib.connectivity.bluetooth.BlueUtils
import com.example.trackingmypantry.lib.credentials.CredentialsHandler
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private val BLUETOOTH_REQUEST_BACKGROUND = 1
    private val BLUETOOTH_REQUEST_COARSE = 2

    // UI elements
    private lateinit var signupButton: AppCompatButton
    private lateinit var signinButton: AppCompatButton
    private lateinit var logoutButton: AppCompatButton
    private lateinit var localButton: AppCompatButton
    private lateinit var cameraButton: AppCompatImageButton
    private lateinit var searchButton: AppCompatButton
    private lateinit var barcodeText: EditText
    private lateinit var suggestButton: AppCompatButton
    private lateinit var locationsButton: AppCompatButton

    // Activity launchers
    private val signupLauncher = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                Utils.toastShow(this, "You are now registered to the service!")
            }

            ResultCode.NETWORK_ERR -> {
                Utils.toastShow(this, "Registration failure")
            }

            ResultCode.EXISTENT_USER -> {
                Utils.toastShow(this, "Username already in use")
            }
        }
    }
    private val signinLauncher = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                this.logoutButton.visibility = android.view.View.VISIBLE
                this.searchButton.visibility = android.view.View.VISIBLE
                this.cameraButton.visibility = android.view.View.VISIBLE
                this.barcodeText.visibility = android.view.View.VISIBLE
                Utils.toastShow(this, "You are now signed in!")
            }

            ResultCode.NETWORK_ERR -> {
                Utils.toastShow(this, "Access failure")
            }
        }
    }
    private val buyLauncher = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                // do nothing
            }

            ResultCode.NETWORK_ERR, ResultCode.INVALID_SESSION_TOKEN -> {
                Utils.toastShow(this, "It's not possible to carry out this action")
            }

            ResultCode.EXPIRED_TOKEN -> {
                val intent = Intent(this, SignInActivity::class.java)
                intent.putExtra("retry", true)
                this.signinLauncher.launch(intent)
            }
        }
    }
    private val barcodeScanLauncher = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                var intent = Intent(this, BuyActivity::class.java)
                intent.putExtra("barcode", result.data!!.extras!!["barcode"] as String)
                this.buyLauncher.launch(intent)
            }

            RESULT_CANCELED -> {
                // do nothing
            }

            ResultCode.DENIED_PERMISSIONS -> {
                Utils.toastShow(this, "Do not have permissions to do this operation")
            }
        }
    }

    private fun showPermissionsInfo(performRequest: Boolean) {
        AlertDialog.Builder(this)
            .setTitle("SENSITIVE PERMISSIONS")
            .setMessage(
                "In order to use this functionality you have to grant location permission. " +
                "By clicking 'OK', a screen where you are asked to grant this type of " +
                "permission will be displayed. If this is not going to happen, you have to " +
                "navigate the android settings and grant the permission from there. Anyway, " +
                "it's important that you select the entry 'ALLOW ALL THE TIME'"
            )
            .setPositiveButton(R.string.positiveOk, DialogInterface.OnClickListener() { _, _ ->
                if (performRequest) {
                    PermissionEvaluer.request(
                        this,
                        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        BLUETOOTH_REQUEST_BACKGROUND
                    )
                }
            })
            .show()
    }

    private fun locationCheck() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q &&
            !PermissionEvaluer.got(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            this.showPermissionsInfo(true)
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q &&
            !PermissionEvaluer.got(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            this.showPermissionsInfo(false)
        } else {
            this.startActivity(Intent(this, BluetoothManagerActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.startService(Intent(this, ClearCredentialsService::class.java))

        this.setContentView(R.layout.activity_main)
        this.signupButton = this.findViewById(R.id.signupButton)
        this.signinButton = this.findViewById(R.id.signinButton)
        this.logoutButton = this.findViewById(R.id.logoutButton)
        this.localButton = this.findViewById(R.id.localButton)
        this.cameraButton = this.findViewById(R.id.cameraButton)
        this.searchButton = this.findViewById(R.id.searchButton)
        this.barcodeText = this.findViewById(R.id.barcodeText)
        this.suggestButton = this.findViewById(R.id.suggestionsButton)
        this.locationsButton = this.findViewById(R.id.locationsButton)

        if (!BlueUtils.hasBluetoothFeature(this)) {
            this.suggestButton.visibility = android.view.View.GONE
        }

        if (TokenHandler.getToken(this, TokenType.ACCESS, false) == TokenHandler.INEXISTENT_TOKEN) {
            this.searchButton.visibility = android.view.View.GONE
            this.cameraButton.visibility = android.view.View.GONE
            this.barcodeText.visibility = android.view.View.GONE
            this.logoutButton.visibility = android.view.View.GONE
        }

        this.signupButton.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            this.signupLauncher.launch(intent)
        }

        this.signinButton.setOnClickListener {
            var intent = Intent(this, SignInActivity::class.java)
            this.signinLauncher.launch(intent)
        }

        this.searchButton.setOnClickListener {
            if (Utils.stringPattern(EvalMode.EMPTY, barcodeText.text.toString())) {
                barcodeText.requestFocus()
            } else {
                var intent = Intent(this, BuyActivity::class.java)
                intent.putExtra("barcode", barcodeText.text.toString())
                this.buyLauncher.launch(intent)
            }
        }

        this.localButton.setOnClickListener {
            this.startActivity(Intent(this, CollectionsActivity::class.java))
        }

        this.cameraButton.setOnClickListener {
            var intent = Intent(this, BarcodeScannerActivity::class.java)
            intent.putExtra("imageCapturing", false)
            this.barcodeScanLauncher.launch(intent)
        }

        this.logoutButton.setOnClickListener {
            TokenHandler.removeToken(this, TokenType.ACCESS)

            this.searchButton.visibility = android.view.View.GONE
            this.cameraButton.visibility = android.view.View.GONE
            this.barcodeText.visibility = android.view.View.GONE
            this.logoutButton.visibility = android.view.View.GONE
        }

        this.suggestButton.setOnClickListener {
            if (!PermissionEvaluer.got(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                PermissionEvaluer.request(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    BLUETOOTH_REQUEST_COARSE
                )
            } else {
                this.locationCheck()
            }
        }

        this.locationsButton.setOnClickListener {
            val actionButtons = RadioGroup(this)
            val addLocationsButton = RadioButton(this)
            val removeLocationsButton = RadioButton(this)

            addLocationsButton.text = "Add new locations"
            removeLocationsButton.text = "Remove your locations"

            actionButtons.addView(addLocationsButton)
            actionButtons.addView(removeLocationsButton)

            AlertDialog.Builder(this)
                .setTitle("Choose action")
                .setMessage("Choose one of the following action")
                .setView(actionButtons)
                .setNegativeButton(R.string.negativeCanc, null)
                .setPositiveButton(R.string.choose, DialogInterface.OnClickListener { _, _ ->
                    if (addLocationsButton.isChecked) {
                        this.startActivity(Intent(this, LocationsActivity::class.java))
                    } else if (removeLocationsButton.isChecked) {
                        //TODO: start new activity
                    }
                })
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            BLUETOOTH_REQUEST_BACKGROUND -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.startActivity(Intent(this, BluetoothManagerActivity::class.java))
                } else {
                    Utils.toastShow(this, "Do not have permissions")
                }
            }

            BLUETOOTH_REQUEST_COARSE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.locationCheck()
                } else {
                    Utils.toastShow(this, "Do not have permissions")
                }
            }
        }
    }
}