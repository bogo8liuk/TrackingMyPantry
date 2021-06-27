package com.example.trackingmypantry.lib

import android.content.Context

class BuyHandler() {
    companion object {
        private fun serviceNotify() {

        }

        fun buy(context: Context, barcode: String) {
            HttpHandler.serviceGetProduct(
                context,
                barcode,
                "00000000", // TODO: get the right access token
                { res ->
                    // TODO: implement a ViewModel
                },
                { status_code, _ ->

                }
            )
        }
    }
}