package com.example.trackingmypantry

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import com.example.trackingmypantry.lib.credentials.TokenHandler
import com.example.trackingmypantry.lib.credentials.TokenType
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.ResultCode
import com.example.trackingmypantry.lib.credentials.CredentialsHandler

class MainActivity : AppCompatActivity() {
    // UI elements
    private lateinit var signupButton: AppCompatButton
    private lateinit var signinButton: AppCompatButton
    private lateinit var logoutButton: AppCompatButton
    private lateinit var localButton: AppCompatButton
    private lateinit var cameraButton: AppCompatImageButton
    private lateinit var searchButton: AppCompatButton
    private lateinit var barcodeText: EditText

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
                Utils.toastShow(this, "Network failure")
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
                var intent = Intent()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setContentView(R.layout.activity_main)
        this.signupButton = this.findViewById(R.id.signupButton)
        this.signinButton = this.findViewById(R.id.signinButton)
        this.logoutButton = this.findViewById(R.id.logoutButton)
        this.localButton = this.findViewById(R.id.localButton)
        this.cameraButton = this.findViewById(R.id.cameraButton)
        this.searchButton = this.findViewById(R.id.searchButton)
        this.barcodeText = this.findViewById(R.id.barcodeText)

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
            if (barcodeText.text.equals("")) {
                barcodeText.requestFocus()
            } else {
                var intent = Intent(this, BuyActivity::class.java)
                intent.putExtra("barcode", barcodeText.text.toString())
                this.buyLauncher.launch(intent)
            }
        }

        this.localButton.setOnClickListener {
            this.startActivity(Intent(this, LocalItemsActivity::class.java))
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
    }
}