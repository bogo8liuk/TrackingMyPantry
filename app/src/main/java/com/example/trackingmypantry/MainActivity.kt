package com.example.trackingmypantry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import com.example.trackingmypantry.lib.TokenHandler
import com.example.trackingmypantry.lib.TokenType
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.net.ResultCode
import java.io.File

class MainActivity : AppCompatActivity() {
    private var accessToken: String = TokenHandler.INEXISTENT_TOKEN

    private val REGISTER_REQ_CODE = 0
    private val AUTH_REQ_CODE = 1

    private lateinit var signupButton: AppCompatButton
    private lateinit var signinButton: AppCompatButton
    private lateinit var localButton: AppCompatButton
    private lateinit var cameraButton: AppCompatImageButton
    private lateinit var searchButton: AppCompatButton
    private lateinit var barcodeText: EditText

    private fun getLogInfo() {
        accessToken = TokenHandler.getToken(this, TokenType.ACCESS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.getLogInfo()

        this.setContentView(R.layout.activity_main)
        signupButton = this.findViewById(R.id.signupButton)
        signinButton = this.findViewById(R.id.signinButton)
        localButton = this.findViewById(R.id.localButton)
        cameraButton = this.findViewById(R.id.cameraButton)
        searchButton = this.findViewById(R.id.searchButton)
        barcodeText = this.findViewById(R.id.barcodeText)

        signupButton.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            this.startActivityForResult(intent, REGISTER_REQ_CODE)
        }

        signinButton.setOnClickListener {
            var intent = Intent(this, SignInActivity::class.java)
            this.startActivity(intent)
        }

        searchButton.setOnClickListener {
            var intent = Intent(this, ChooseActionActivity::class.java)
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

                    ResultCode.NETWORK_ERR -> {
                        Utils.toastShow(this, "Registration failure")
                    }

                    ResultCode.EXISTENT_USER -> {
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