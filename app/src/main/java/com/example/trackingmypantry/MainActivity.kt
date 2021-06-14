package com.example.trackingmypantry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.lib.Utils
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private val REGISTER_REQ_CODE = 0

    private lateinit var buyButton: AppCompatButton
    private lateinit var signupButton: AppCompatButton
    private lateinit var signinButton: AppCompatButton
    private lateinit var cameraButton: AppCompatButton
    private lateinit var barcodeText: EditText

    /*
     * Do not call this function before onCreate()
     */
    private fun setButtonsVisibility() {
        if (Utils.isLogged(this)) {
            buyButton.visibility = android.view.View.VISIBLE
            signupButton.visibility = android.view.View.GONE
            signinButton.visibility = android.view.View.GONE
            cameraButton.visibility = android.view.View.VISIBLE
            barcodeText.visibility = android.view.View.VISIBLE
        } else {
            buyButton.visibility = android.view.View.GONE
            signupButton.visibility = android.view.View.VISIBLE
            signinButton.visibility = android.view.View.VISIBLE
            cameraButton.visibility = android.view.View.GONE
            barcodeText.visibility = android.view.View.GONE
        }
    }

    private fun createLogOnNotExist() {
        val logFile = File(this.filesDir, "log.json")

        if (!logFile.exists()) {
            this.openFileOutput(logFile.name, Context.MODE_PRIVATE).use {
                it.write("""
{
    "status": "no",
    "accessToken": "null"
}
                """.trimIndent().encodeToByteArray())
            }
        }
    }

    private fun getLogInfo(): Pair<String, String> {
        return Pair(Utils.getLoginStatus(this), Utils.getLoginToken(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.createLogOnNotExist()

        this.setContentView(R.layout.activity_main)
        buyButton = findViewById<AppCompatButton>(R.id.buy_button)
        signupButton = findViewById<AppCompatButton>(R.id.signup_button)
        signinButton = findViewById<AppCompatButton>(R.id.signin_button)
        cameraButton = findViewById<AppCompatButton>(R.id.camera_button)
        barcodeText = findViewById<EditText>(R.id.barcode_text)

        buyButton.setOnClickListener {
            var intent = Intent(this, BuyerActivity::class.java)
            this.startActivity(intent)
        }

        signupButton.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            this.startActivityForResult(intent, REGISTER_REQ_CODE)
        }

        signinButton.setOnClickListener {
            var intent = Intent(this, SignInActivity::class.java)
            this.startActivity(intent)
        }

        this.setButtonsVisibility()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REGISTER_REQ_CODE -> {
                this.setButtonsVisibility()
                when (resultCode) {
                    RESULT_OK -> {
                        Utils.toastShow(this, "You are now registered to the service!")
                    }
                    Utils.ResultCode.NETWORK_ERR -> {
                        Utils.toastShow(this, "Registration failure")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}