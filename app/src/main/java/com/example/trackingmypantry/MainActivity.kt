package com.example.trackingmypantry

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buyButton = findViewById<AppCompatButton>(R.id.buy_button)
        buyButton.setOnClickListener {
            fun onClick(view: View) {
                var intent = Intent(this, BuyerActivity::class.java)
                this.startActivity(intent)
            }
        }

        var signupButton = findViewById<AppCompatButton>(R.id.signup_button)
        signupButton.setOnClickListener {
            fun onClick(view: View) {
                var intent = Intent(this, SignUpActivity::class.java)
                this.startActivity(intent)
            }
        }

        var signinButton = findViewById<AppCompatButton>(R.id.signin_button)
        signinButton.setOnClickListener {
            fun onClick(view: View) {
                var intent = Intent(this, SignInActivity::class.java)
                this.startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}