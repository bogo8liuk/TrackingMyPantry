package com.example.trackingmypantry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.lib.Utils

class MainActivity : AppCompatActivity() {
    private val AUTH_REQ_CODE = 0
    private var buyButton = findViewById<AppCompatButton>(R.id.buy_button)
    var signupButton = findViewById<AppCompatButton>(R.id.signup_button)
    var signinButton = findViewById<AppCompatButton>(R.id.signin_button)

    private fun setButtonsVisibility() {
        if (Utils.isLogged(this)) {
            buyButton.visibility = android.view.View.VISIBLE
            signupButton.visibility = android.view.View.GONE
            signinButton.visibility = android.view.View.GONE
            // TODO: the other buttons
        } else {
            buyButton.visibility = android.view.View.GONE
            signupButton.visibility = android.view.View.VISIBLE
            signinButton.visibility = android.view.View.VISIBLE
            // TODO: the other buttons
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buyButton.setOnClickListener {
            var intent = Intent(this, BuyerActivity::class.java)
            this.startActivity(intent)
        }

        signupButton.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            this.startActivityForResult(intent, AUTH_REQ_CODE)
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
            AUTH_REQ_CODE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        buyButton.visibility = android.view.View.VISIBLE
                        signinButton.visibility = android.view.View.GONE
                        signupButton.visibility = android.view.View.GONE
                        Utils.toastShow(this, "You are now registered to the service!")
                    }
                    Utils.ResultCode.NETWORK_ERR -> {
                        /*TODO*/
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