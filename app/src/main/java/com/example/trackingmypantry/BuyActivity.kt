package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trackingmypantry.lib.HttpHandler
import com.example.trackingmypantry.lib.Utils

class BuyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)

        HttpHandler.serviceGetProduct(
            this,
            this.intent.extras!!.getString("barcode", Utils.DEFAULT_BARCODE),
            "00000000", // TODO: get the right access token
            { res ->

            },
            { status_code, _ ->

            }
        )
    }
}