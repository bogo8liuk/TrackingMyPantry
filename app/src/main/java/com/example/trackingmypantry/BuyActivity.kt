package com.example.trackingmypantry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.lib.adapters.ReceivedItemsAdapter
import com.example.trackingmypantry.lib.data.*
import com.example.trackingmypantry.lib.viewmodels.ReceivedItemsViewModel
import com.example.trackingmypantry.lib.viewmodels.ReceivedItemsViewModelFactory

class BuyActivity() : AppCompatActivity() {
    private lateinit var descriptionTextView: TextView
    private lateinit var sadnessImageView: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var newProdButton: AppCompatButton

    private lateinit var barcode: String

    private val addDescLauncher = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
            val intent = Intent()
            this.setResult(result.resultCode, intent)
            this.finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_buy)

        this.descriptionTextView = this.findViewById(R.id.buyDescription)
        this.sadnessImageView = this.findViewById(R.id.sadnessImage)
        this.newProdButton = this.findViewById(R.id.newProdButton)
        this.recyclerView = this.findViewById(R.id.receivedItemsRecView)

        this.recyclerView.adapter = ReceivedItemsAdapter(arrayOf<Product>())    // To avoid layout skipping
        this.recyclerView.layoutManager = LinearLayoutManager(this)

        this.barcode = this.intent.extras?.get("barcode") as String

        this.newProdButton.setOnClickListener {
            val intent = Intent(this, AddDescriptionActivity::class.java)
            intent.putExtra("barcode", this.barcode)
            this.addDescLauncher.launch(intent)
        }

        val model: ReceivedItemsViewModel by viewModels {
            ReceivedItemsViewModelFactory(this.application, this.barcode)
        }
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
                this.recyclerView.adapter = adapter
            }
        })
    }
}