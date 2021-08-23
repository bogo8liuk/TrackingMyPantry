package com.example.trackingmypantry.lib

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.lang.Exception

class Utils {
    companion object {
        fun toastShow(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        fun base64ToBitmap(base64: String): Bitmap? {
            return try {
                val decoded = Base64.decode(base64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
            } catch (exception: Exception) {
                null
            }
        }

        fun bitmapToBase64(bitmap: Bitmap): String? {
            return try {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
            } catch (exception: Exception) {
                null
            }
        }
    }
}