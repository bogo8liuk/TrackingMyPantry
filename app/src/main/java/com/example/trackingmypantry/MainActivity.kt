package com.example.trackingmypantry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        var buy_button = findViewById<Button>(R.id.buy_button)
        buy_button.setOnClickListener(View.OnClickListener {
            fun onClick(view: View) {
                intent = Intent()
            }
        })
    }
}