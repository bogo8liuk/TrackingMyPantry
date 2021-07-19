package com.example.trackingmypantry

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.net.HttpHandler

class RateActivity : AppCompatActivity() {
    private lateinit var sendButton: AppCompatButton
    private lateinit var barcodeText: TextView
    private lateinit var nameText: TextView
    private lateinit var ratePicker: NumberPicker
    private lateinit var inputText: TextView

    private val MIN_RATE = 1
    private val MAX_RATE = 5

    /* Implementing here because there are two points where this function is called */
    private val sendCallback = { prodId: String ->

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_rate)

        this.sendButton = this.findViewById(R.id.sendButton)
        this.barcodeText = this.findViewById(R.id.barcodeDescText)
        this.nameText = this.findViewById(R.id.nameDescText)
        this.ratePicker = this.findViewById(R.id.ratePicker)
        this.inputText = this.findViewById(R.id.inputTextMultiLine)

        val extras = this.intent.extras
        this.barcodeText.text = this.getString(R.string.barcode) + ": " + extras!!["barcode"]
        this.nameText.text = this.getString(R.string.name) + ": " + extras!!["name"]

        this.ratePicker.minValue = MIN_RATE
        this.ratePicker.maxValue = MAX_RATE

        this.sendButton.setOnClickListener {
            /*if (this.inputText.text.toString().all { c -> c == ' ' }) { // No text inserted
                AlertDialog.Builder(this)
                    .setMessage("Are you sure of keeping an empty description?")
                    .setTitle("No description provided")
                    .setNegativeButton(R.string.negative, null)
                    .setPositiveButton(R.string.positive, DialogInterface.OnClickListener { _, _ ->
                        this.sendCallback(extras!!["productId"] as String)
                    })
            } else {

            }*/
            HttpHandler.serviceVoteProduct(
                this,
                "TODO", //TODO: session token
                this.ratePicker.value,
                extras!!["productId"] as String,
                { res ->

                },
                { statusCode, err ->

                }
            )
        }
    }
}