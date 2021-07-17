package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.lib.ReceivedItemsAdapter
import com.example.trackingmypantry.lib.data.ERR_FIELD
import com.example.trackingmypantry.lib.data.Product
import com.example.trackingmypantry.lib.data.special_err_product
import com.example.trackingmypantry.lib.viewModel.ReceivedItemsViewModel
import com.example.trackingmypantry.lib.viewModel.ReceivedItemsViewModelFactory

class BuyActivity : AppCompatActivity() {
    private lateinit var descriptionTextView: TextView
    private lateinit var sadnessImageView: ImageView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_buy)
        this.descriptionTextView = this.findViewById(R.id.buyDescription)
        this.sadnessImageView = this.findViewById(R.id.sadnessImage)
        this.recyclerView = this.findViewById(R.id.receivedItemsRecView)

        val model: ReceivedItemsViewModel by viewModels //TODO: pass a factory
        model.getReceivedItems().observe(this, Observer<List<Product>> {
            if (it.any { product -> product.barcode == ERR_FIELD }) {
                this.descriptionTextView.text = "Sorry, you have came across a network failure"
                this.sadnessImageView.visibility = android.view.View.VISIBLE
            } else if (it.isEmpty()) {
                this.descriptionTextView.text = "No products available, be sure to enter details" +
                    " about the product you are buying in order to notify the service"
            } else {
                this.descriptionTextView.text = "Choose the product you wish to buy or notify the" +
                    " server about a new one"
                val adapter = ReceivedItemsAdapter(it.toTypedArray())
                recyclerView.adapter = adapter
            }
        })
    }
}