package com.example.trackingmypantry

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
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

class MainActivity : AppCompatActivity() {
    companion object {
        private const val BLUETOOTH_REQUEST_BACKGROUND = 1
        private const val BLUETOOTH_REQUEST_COARSE = 2

        const val SCANNED_BARCODE_EXTRA = "barcode"
    }

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
                val logoutButton: AppCompatButton = this.findViewById(R.id.logoutButton)
                val searchButton: AppCompatButton = this.findViewById(R.id.searchButton)
                val cameraButton: AppCompatImageButton = this.findViewById(R.id.cameraButton)
                val barcodeText: TextView = this.findViewById(R.id.barcodeText)

                logoutButton.visibility = android.view.View.VISIBLE
                searchButton.visibility = android.view.View.VISIBLE
                cameraButton.visibility = android.view.View.VISIBLE
                barcodeText.visibility = android.view.View.VISIBLE
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
                intent.putExtra(SignInActivity.RETRY_EXTRA, true)
                this.signinLauncher.launch(intent)
            }
        }
    }
    private val barcodeScanLauncher = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                var intent = Intent(this, BuyActivity::class.java)
                intent.putExtra(BuyActivity.BARCODE_EXTRA, result.data!!.extras!![SCANNED_BARCODE_EXTRA] as String)
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
    private val enablingBtLauncher = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                this.coarseLocationCheck()
            }

            RESULT_CANCELED -> {
                Utils.toastShow(this, "Enable bluetooth to use this functionality")
            }
        }
    }

    private fun coarseLocationCheck() {
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
        val signupButton: AppCompatButton = this.findViewById(R.id.signupButton)
        val signinButton: AppCompatButton = this.findViewById(R.id.signinButton)
        val logoutButton: AppCompatButton = this.findViewById(R.id.logoutButton)
        val localButton: AppCompatButton = this.findViewById(R.id.localButton)
        val cameraButton: AppCompatImageButton = this.findViewById(R.id.cameraButton)
        val searchButton: AppCompatButton = this.findViewById(R.id.searchButton)
        val barcodeText: TextView = this.findViewById(R.id.barcodeText)
        val suggestButton: AppCompatButton = this.findViewById(R.id.bluetoothButton)
        val locationsButton: AppCompatButton = this.findViewById(R.id.locationsButton)

        if (!BlueUtils.hasBluetoothFeature(this)) {
            suggestButton.visibility = android.view.View.GONE
        }

        if (TokenHandler.getToken(this, TokenType.ACCESS, false) == TokenHandler.INEXISTENT_TOKEN) {
            searchButton.visibility = android.view.View.GONE
            cameraButton.visibility = android.view.View.GONE
            barcodeText.visibility = android.view.View.GONE
            logoutButton.visibility = android.view.View.GONE
        }

        signupButton.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            this.signupLauncher.launch(intent)
        }

        signinButton.setOnClickListener {
            var intent = Intent(this, SignInActivity::class.java)
            this.signinLauncher.launch(intent)
        }

        searchButton.setOnClickListener {
            if (Utils.stringPattern(EvalMode.EMPTY, barcodeText.text.toString())) {
                barcodeText.requestFocus()
            } else {
                var intent = Intent(this, BuyActivity::class.java)
                intent.putExtra(BuyActivity.BARCODE_EXTRA, barcodeText.text.toString())
                this.buyLauncher.launch(intent)
            }
        }

        localButton.setOnClickListener {
            this.startActivity(Intent(this, CollectionsActivity::class.java))
        }

        cameraButton.setOnClickListener {
            this.barcodeScanLauncher.launch(Intent(this, BarcodeScannerActivity::class.java))
        }

        logoutButton.setOnClickListener {
            TokenHandler.removeToken(this, TokenType.ACCESS)

            searchButton.visibility = android.view.View.GONE
            cameraButton.visibility = android.view.View.GONE
            barcodeText.visibility = android.view.View.GONE
            logoutButton.visibility = android.view.View.GONE
        }

        suggestButton.setOnClickListener {
            val btAdapter = BlueUtils.bluetoothAdapter(this)
            if (BlueUtils.enableRequestIfDisabled(btAdapter, this.enablingBtLauncher)) {
                this.coarseLocationCheck()
            }
        }

        locationsButton.setOnClickListener {
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
                        this.startActivity(Intent(this, RemoveLocationsActivity::class.java))
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