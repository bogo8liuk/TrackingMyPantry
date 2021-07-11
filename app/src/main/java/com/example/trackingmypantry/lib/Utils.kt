package com.example.trackingmypantry.lib

import android.content.Context
import android.widget.Toast
import com.example.trackingmypantry.lib.data.Product
import org.json.JSONObject

class Utils {
    companion object {
        fun toastShow(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}