package com.example.trackingmypantry

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.credentials.TokenHandler
import com.example.trackingmypantry.lib.credentials.TokenType
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.net.HttpHandler
import com.example.trackingmypantry.lib.ResultCode
import com.example.trackingmypantry.lib.credentials.CredentialsHandler
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AddDescriptionActivity : CameraLauncherActivity() {
    private lateinit var descText: TextView
    private lateinit var descEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var expirationEdiText: EditText
    private lateinit var sendButton: AppCompatButton
    private lateinit var photoButton: AppCompatButton
    private lateinit var image: ImageView

    /* Implementing here because there are two points where this function is called:
    * avoiding boiler-plate code. */
    private val send = { barcode: String ->
        HttpHandler.retryOnFailure(
            HttpHandler.RequestType.DESCRIBE,
            this,
            { res ->
                val expiration = this.expirationEdiText.text
                val encoded = this.encodedImage?.let { Utils.bitmapToBase64(it) }

                res as JSONObject
                if (expiration == null) {
                    DbSingleton.getInstance(this).insertItems(
                        Item(
                            0,
                            res.getString("barcode"),
                            res.getString("name"),
                            res.getString("description"),
                            encoded,
                            Date(),
                            null,
                            null
                        )
                    )
                } else {
                    DbSingleton.getInstance(this).insertItems(
                        Item(
                            0,
                            res.getString("barcode"),
                            res.getString("name"),
                            res.getString("description"),
                            encoded,
                            Date(),
                            SimpleDateFormat("yyyy-MM-dd").parse(this.expirationEdiText.text.toString()),
                            null
                        )
                    )
                }

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
            },
            JSONObject("{ \"name\": \"${this.nameEditText.text.toString()}\", " +
                    "\"description\": \"${this.descEditText.text.toString()}\", " +
                    "\"barcode\": \"$barcode\" }")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_add_description)

        this.descText = this.findViewById(R.id.descText)
        this.descEditText = this.findViewById(R.id.descEditText)
        this.nameEditText = this.findViewById(R.id.nameEditText)
        this.expirationEdiText = this.findViewById(R.id.expirationEditText)
        this.sendButton = this.findViewById(R.id.sendButton)
        this.photoButton = this.findViewById(R.id.photoButton)
        this.image = this.findViewById(R.id.productImage)

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

        this.photoButton.setOnClickListener {
            this.cameraLaunch({ bitmap ->  
                image.setImageBitmap(bitmap)
            }, {
                // do nothing
            })
        }
    }
}