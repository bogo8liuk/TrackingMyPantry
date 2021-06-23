package com.example.trackingmypantry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class ChooseActionActivity: AppCompatActivity() {
    private lateinit var buyButton: AppCompatButton
    private lateinit var localButton: AppCompatButton
    private lateinit var shareButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setContentView(R.layout.activity_choose)
        buyButton = this.findViewById(R.id.buyButton)
        localButton = this.findViewById(R.id.localButton2)
        shareButton = this.findViewById(R.id.shareButton)

        buyButton.setOnClickListener {

        }
    }
}