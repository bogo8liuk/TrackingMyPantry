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

        var buy_button = findViewById<AppCompatButton>(R.id.buy_button)
        buy_button.setOnClickListener {
            fun onClick(view: View) {
                var intent = Intent(this, BuyerActivity::class.java)
                startActivity(intent)
            }
        }

        var signup_button = findViewById<AppCompatButton>(R.id.signup_button)
        signup_button.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()


    }
}