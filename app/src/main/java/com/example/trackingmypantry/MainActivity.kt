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
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private var loginStatus = "no"
    private var accessToken: String? = null

    private val REGISTER_REQ_CODE = 0
    private val AUTH_REQ_CODE = 1

    private lateinit var buyButton: AppCompatButton
    private lateinit var signupButton: AppCompatButton
    private lateinit var signinButton: AppCompatButton
    private lateinit var cameraButton: AppCompatButton
    private lateinit var barcodeText: EditText

    private fun createLogOnNotExist() {
        val logFile = File(this.filesDir, Utils.logFileName)

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

    /* TODO: Putting it in a viewmodel. */
    private fun getLogInfo() {
        loginStatus = Utils.getLoginStatus(this)
        accessToken = Utils.getLoginToken(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.createLogOnNotExist()
        this.getLogInfo()   //TODO: temporary call, see getLogInfo() comment

        this.setContentView(R.layout.activity_main)
        buyButton = findViewById(R.id.buyButton)
        signupButton = findViewById(R.id.signupButton)
        signinButton = findViewById(R.id.signinButton)
        cameraButton = findViewById(R.id.camera_button)
        barcodeText = findViewById(R.id.barcode_text)

        buyButton.setOnClickListener {
            if (Utils.isLogged(loginStatus)) {
                var intent = Intent(this, BuyerActivity::class.java)
                this.startActivity(intent)
            } else {
                Utils.toastShow(this, "First sign in to buy a product")
            }
        }

        signupButton.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            this.startActivityForResult(intent, REGISTER_REQ_CODE)
        }

        signinButton.setOnClickListener {
            var intent = Intent(this, SignInActivity::class.java)
            this.startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REGISTER_REQ_CODE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        Utils.toastShow(this, "You are now registered to the service!")
                    }

                    Utils.ResultCode.NETWORK_ERR -> {
                        Utils.toastShow(this, "Registration failure")
                    }

                    Utils.ResultCode.EXISTENT_USER -> {
                        Utils.toastShow(this, "Username already in use")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}