package com.example.trackingmypantry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.lib.Utils

class MainActivity : AppCompatActivity() {
    private val AUTH_REQ_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buyButton = findViewById<AppCompatButton>(R.id.buy_button)
        buyButton.setOnClickListener {
            var intent = Intent(this, BuyerActivity::class.java)
            this.startActivity(intent)
        }

        var signupButton = findViewById<AppCompatButton>(R.id.signup_button)
        signupButton.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            this.startActivityForResult(intent, AUTH_REQ_CODE)
        }

        var signinButton = findViewById<AppCompatButton>(R.id.signin_button)
        signinButton.setOnClickListener {
            var intent = Intent(this, SignInActivity::class.java)
            this.startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            AUTH_REQ_CODE -> {
                when (resultCode) {
                    RESULT_OK -> {/*TODO*/}
                    Utils.ResultCode.NETWORK_ERR -> {/*TODO*/}
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}