package com.example.trackingmypantry

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class ChooseActionActivity: AppCompatActivity() {
    private val DEFAULT_BARCODE = "00000000"
    private lateinit var barcode: String
    private lateinit var buyButton: AppCompatButton
    private lateinit var localButton: AppCompatButton
    private lateinit var shareButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setContentView(R.layout.activity_choose)
        barcode = this.intent.extras?.getString("barcode", DEFAULT_BARCODE) ?: DEFAULT_BARCODE
        buyButton = this.findViewById(R.id.buyButton)
        localButton = this.findViewById(R.id.localButton2)
        shareButton = this.findViewById(R.id.shareButton)

        buyButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.buy1)
                .setMessage("Are you sure of the purchase?")
                .setNegativeButton(R.string.negative, null)
                .setPositiveButton(R.string.positive, DialogInterface.OnClickListener { dialog, which ->
                    val intent = Intent(this, BuyActivity::class.java)
                    intent.putExtra("barcode", barcode)
                    this.startActivity(intent)
                })
                .show()
        }
    }
}