package com.example.trackingmypantry

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.TokenHandler
import com.example.trackingmypantry.lib.TokenType
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.net.HttpHandler
import com.example.trackingmypantry.lib.net.ResultCode
import java.util.*

class AddDescriptionActivity : AppCompatActivity() {
    private lateinit var descText: TextView
    private lateinit var descEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var sendButton: AppCompatButton

    /* Implementing here because there are two points where this function is called:
    * avoiding boiler-plate code. */
    private val send = { barcode: String ->
        HttpHandler.serviceDescribeProduct(
            this,
            TokenHandler.getToken(this, TokenType.ACCESS),
            TokenHandler.getToken(this, TokenType.SESSION),
            this.nameEditText.text.toString(),
            this.descEditText.text.toString(),
            barcode,
            { res ->
                val item = Item(
                    0,
                    res.getString("barcode"),
                    res.getString("name"),
                    res.getString("description"),
                    null, // TODO: image
                    Date(),
                    null    // TODO: expiration
                )
                DbSingleton.getInstance(this).insertItems(item)

                this.setResult(RESULT_OK, Intent())
                this.finish()
            },
            { statusCode, _ ->
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
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_add_description)

        this.descText = this.findViewById(R.id.descText)
        this.descEditText = this.findViewById(R.id.descEditText)
        this.nameEditText = this.findViewById(R.id.nameEditText)
        this.sendButton = this.findViewById(R.id.sendButton)

        val extras = this.intent.extras

        this.sendButton.setOnClickListener {
            if (this.nameEditText.text.toString() == "") {
                this.nameEditText.requestFocus()
                Utils.toastShow(this, "Name is mandatory")
            } else if (this.descEditText.text.toString().all { c -> c == ' ' }) { // No text inserted
                AlertDialog.Builder(this)
                    .setMessage("Are you sure of keeping an empty description?")
                    .setTitle("No description provided")
                    .setNegativeButton(R.string.negative, null)
                    .setPositiveButton(R.string.positive, DialogInterface.OnClickListener { _, _ ->
                        this.send(extras!!["barcode"] as String)
                    })
            } else {
                this.send(extras!!["barcode"] as String)
            }
        }
    }
}