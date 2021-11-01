package com.example.trackingmypantry

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.EvalMode
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.connectivity.net.HttpHandler
import com.example.trackingmypantry.lib.ResultCode
import org.json.JSONObject
import java.util.*

class AddDescriptionActivity : CameraLauncherActivity() {
    companion object {
        const val BARCODE_EXTRA = "barcode"
    }

    private var expirationDate: Date? = null

    /* Implementing here because there are two points where this function is called:
    * avoiding boiler-plate code. */
    private val send = { barcode: String ->
        val name = this.findViewById<EditText>(R.id.nameEditText).text.toString()
        val description = this.findViewById<EditText>(R.id.descEditText).text.toString()

        val encoded = this.encodedImage?.let { Utils.bitmapToBase64(it) }
        HttpHandler.retryOnFailure(
            HttpHandler.RequestType.DESCRIBE,
            this,
            { res ->
                res as JSONObject
                DbSingleton.getInstance(this).insertItems(
                    Item(
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
                name,
                description,
                barcode,
                encoded
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_add_description)

        val descEditText: EditText = this.findViewById(R.id.descEditText)
        val nameEditText: EditText = this.findViewById(R.id.nameEditText)
        val expirationButton: AppCompatButton = this.findViewById(R.id.expirationDateButton)
        val sendButton: AppCompatButton = this.findViewById(R.id.sendButton)
        val photoButton: AppCompatButton = this.findViewById(R.id.photoButton)
        val image: ImageView = this.findViewById(R.id.productImage)

        val extras = this.intent.extras

        sendButton.setOnClickListener {
            if (Utils.stringPattern(EvalMode.EMPTY, nameEditText.text.toString())) {
                nameEditText.requestFocus()
                Utils.toastShow(this, "Name is mandatory")
            } else if (Utils.stringPattern(EvalMode.WHITESPACE, descEditText.text.toString())) { // No text inserted
                AlertDialog.Builder(this)
                    .setMessage("Are you sure of keeping an empty description?")
                    .setTitle("No description provided")
                    .setNegativeButton(R.string.negative, null)
                    .setPositiveButton(R.string.positive, DialogInterface.OnClickListener { _, _ ->
                        this.send(extras!![BARCODE_EXTRA] as String)
                    })
                    .show()
            } else {
                this.send(extras!![BARCODE_EXTRA] as String)
            }
        }

        photoButton.setOnClickListener {
            this.cameraLaunch({ bitmap ->  
                image.setImageBitmap(bitmap)
            }, {
                // do nothing
            })
        }

        expirationButton.setOnClickListener {
            val datePicker = DatePicker(this)
            datePicker.minDate = Date().time
            AlertDialog.Builder(this)
                .setTitle("Expiration date")
                .setMessage("Set an expiration date, it will be saved")
                .setView(datePicker)
                .setNegativeButton(R.string.negativeCanc, null)
                .setPositiveButton(R.string.set, DialogInterface.OnClickListener { _, _ ->
                    this.expirationDate = Calendar.getInstance().also {
                        it.set(datePicker.year, datePicker.month, datePicker.dayOfMonth) }
                        .time
                })
                .show()
        }
    }
}