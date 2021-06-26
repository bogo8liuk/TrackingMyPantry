package com.example.trackingmypantry

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.lib.BuyHandler

class ChooseActionActivity: AppCompatActivity() {
    private val MIN_BUYABLE_ITEMS_NO = 1  // minimum number of objects that can be bought at a time
    private val MAX_BUYABLE_ITEMS_NO = 20 // maximum number of objects that can be bought at a time

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
            var numberPicker = NumberPicker(this)
            numberPicker.minValue = MIN_BUYABLE_ITEMS_NO;
            numberPicker.maxValue = MAX_BUYABLE_ITEMS_NO;
            AlertDialog.Builder(this)
                .setTitle(R.string.buy1)
                .setMessage("Choose the number of items you want to buy")
                .setView(numberPicker)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.buy1, DialogInterface.OnClickListener { dialog, which ->
                    BuyHandler.buy(numberPicker.value)
                })
                .show()
        }
    }
}