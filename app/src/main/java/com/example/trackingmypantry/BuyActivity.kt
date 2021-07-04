package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.trackingmypantry.lib.data.Product
import com.example.trackingmypantry.lib.viewModel.ReceivedItemsViewModel

class BuyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_buy)

        val model: ReceivedItemsViewModel by viewModels()
        model.getReceivedItems().observe(this, Observer<List<Product>> {
            // update UI
        })
    }
}