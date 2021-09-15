package com.example.trackingmypantry

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.EvalMode
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
    private lateinit var expirationButton: AppCompatButton
    private lateinit var sendButton: AppCompatButton
    private lateinit var photoButton: AppCompatButton
    private lateinit var image: ImageView

    private var expirationDate: Date? = null

    /* Implementing here because there are two points where this function is called:
    * avoiding boiler-plate code. */
    private val send = { barcode: String ->
        val encoded = this.encodedImage?.let { Utils.bitmapToBase64(it) }
        HttpHandler.retryOnFailure(
            HttpHandler.RequestType.DESCRIBE,
            this,
            { res ->
                res as JSONObject
                DbSingleton.getInstance(this).insertItems(
                    Item(
                        0,
                        res.getString("barcode"),
                        res.getString("name"),
                        res.getString("description"),
                        encoded,
                        Date(),
                        this.expirationDate,
                        null,
                        1
                    )
                )

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
            describeParams = HttpHandler.Companion.DescribeParams(
                this.nameEditText.text.toString(),
                this.descEditText.text.toString(),
                barcode,
                encoded
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_add_description)

        this.descText = this.findViewById(R.id.descText)
        this.descEditText = this.findViewById(R.id.descEditText)
        this.nameEditText = this.findViewById(R.id.nameEditText)
        this.expirationButton = this.findViewById(R.id.expirationDateButton)
        this.sendButton = this.findViewById(R.id.sendButton)
        this.photoButton = this.findViewById(R.id.photoButton)
        this.image = this.findViewById(R.id.productImage)

        val extras = this.intent.extras

        this.sendButton.setOnClickListener {
            if (Utils.stringPattern(EvalMode.EMPTY, this.nameEditText.text.toString())) {
                this.nameEditText.requestFocus()
                Utils.toastShow(this, "Name is mandatory")
            } else if (Utils.stringPattern(EvalMode.WHITESPACE, this.descEditText.text.toString())) { // No text inserted
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

        this.expirationButton.setOnClickListener {
            val datePicker = DatePicker(this)
            datePicker.minDate = Date().time
            AlertDialog.Builder(this)
                .setTitle("Expiration date")
                .setMessage("Set an expiration date, it will be saved")
                .setView(datePicker)
                .setNegativeButton(R.string.negative1, null)
                .setPositiveButton(R.string.set, DialogInterface.OnClickListener { _, _ ->
                    this.expirationDate = Calendar.getInstance().also {
                        it.set(datePicker.year, datePicker.month, datePicker.dayOfMonth) }
                        .time
                })
                .show()
        }
    }
}