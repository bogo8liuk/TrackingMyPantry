package com.example.trackingmypantry

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.ResultCode
import com.example.trackingmypantry.lib.adapters.IndexedArrayCallback
import com.example.trackingmypantry.lib.adapters.ReceivedItemsAdapter
import com.example.trackingmypantry.lib.connectivity.net.HttpHandler
import com.example.trackingmypantry.lib.data.ERR_FIELD
import com.example.trackingmypantry.lib.data.Product
import com.example.trackingmypantry.lib.viewmodels.ReceivedItemsViewModel
import com.example.trackingmypantry.lib.viewmodels.ReceivedItemsViewModelFactory
import java.util.*

class BuyActivity() : AppCompatActivity() {
    companion object {
        private const val MIN_RATE = 1
        private const val MAX_RATE = 5

        const val BARCODE_EXTRA = "barcode"
    }

    private var expDate: Date? = null
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

        val descriptionTextView: TextView = this.findViewById(R.id.buyDescription)
        val sadnessImageView: ImageView = this.findViewById(R.id.sadnessImage)
        val newProdButton: AppCompatButton = this.findViewById(R.id.newProdButton)
        val recyclerView: RecyclerView = this.findViewById(R.id.receivedItemsRecView)

        recyclerView.adapter = ReceivedItemsAdapter(
            this.buy,
            this.setExpirationAndBuy,
            arrayOf<Product>()
        )    // To avoid layout skipping
        recyclerView.layoutManager = LinearLayoutManager(this)

        this.barcode = this.intent.extras?.get(BARCODE_EXTRA) as String

        newProdButton.setOnClickListener {
            val intent = Intent(this, AddDescriptionActivity::class.java)
            intent.putExtra(AddDescriptionActivity.BARCODE_EXTRA, this.barcode)
            this.addDescLauncher.launch(intent)
        }

        val model: ReceivedItemsViewModel by viewModels {
            ReceivedItemsViewModelFactory(this.application, this.barcode)
        }
        model.getReceivedItems().observe(this, Observer<List<Product>> {
            if (it.any { product -> product.barcode == ERR_FIELD }) {
                descriptionTextView.text = "Sorry, you have came across a network failure"
                sadnessImageView.visibility = android.view.View.VISIBLE
            } else if (it.isEmpty()) {
                descriptionTextView.text = "No products available, be sure to enter details" +
                    " about the product you are buying in order to notify the service"
            } else {
                descriptionTextView.text = "Choose the product you wish to buy or notify the" +
                    " service about a new one"
                val adapter = ReceivedItemsAdapter(
                    this.buy,
                    this.setExpirationAndBuy,
                    it.toTypedArray()
                )
                recyclerView.adapter = adapter
            }
        })
    }

    private val buy: IndexedArrayCallback<Product> = {
        val ratePicker = NumberPicker(this)
        ratePicker.minValue = MIN_RATE
        ratePicker.maxValue = MAX_RATE
        AlertDialog.Builder(this)
            .setTitle("Rate")
            .setMessage("Select a rating for the product you chose")
            .setView(ratePicker)
            .setNegativeButton(R.string.negativeCanc, null)
            .setPositiveButton(R.string.send, DialogInterface.OnClickListener { _, _ ->
                HttpHandler.retryOnFailure(
                    HttpHandler.RequestType.VOTE,
                    this,
                    { res ->
                        DbSingleton.getInstance(this).insertItems(
                            Item(
                                it.array[it.index].barcode,
                                it.array[it.index].name,
                                it.array[it.index].description,
                                it.array[it.index].image,
                                Date(),
                                this.expDate,
                                null,
                                1
                            )
                        )

                        this.setResult(RESULT_OK, Intent())
                        this.finish()
                    },
                    { statusCode, err ->
                        if (statusCode == 401) {
                            this.setResult(ResultCode.EXPIRED_TOKEN, Intent())
                            this.finish()
                        } else if (statusCode == 403) {
                            this.setResult(ResultCode.INVALID_SESSION_TOKEN, Intent())
                            this.finish()
                        } else {
                            this.setResult(ResultCode.NETWORK_ERR, Intent())
                            this.finish()
                        }
                    },
                    voteParams = HttpHandler.Companion.VoteParams(
                        ratePicker.value,
                        it.array[it.index].id
                    )
                )
            })
            .show()
    }

    private val setExpirationAndBuy: IndexedArrayCallback<Product> = {
        var datePicker = DatePicker(this)
        datePicker.minDate = Date().time    // A product cannot be already expired
        AlertDialog.Builder(this)
            .setTitle("Expiration date")
            .setMessage("Set an expiration date for the product")
            .setView(datePicker)
            .setNegativeButton(R.string.negativeCanc, null)
            .setPositiveButton(R.string.set, DialogInterface.OnClickListener { _, _ ->
                this.expDate = Calendar.getInstance().also {
                    it.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
                }.time

                this.buy(it)
            })
            .show()
    }
}