package com.example.trackingmypantry.lib

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import com.example.trackingmypantry.db.entities.Item
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

        private fun intToByteArray(i: Int): ByteArray {
            val buffer = ByteArray(4)

            buffer[0] = (i shr 0).toByte()
            buffer[1] = (i shr 8).toByte()
            buffer[2] = (i shr 16).toByte()
            buffer[3] = (i shr 24).toByte()

            return buffer
        }

        fun itemToByteArray(item: Item): ByteArray {
            val encBarcode = item.barcode.toByteArray()
            val lenBarcode = intToByteArray(encBarcode.size)
            //TODO: finish
        }

        fun stringPattern(mode: EvalMode, s: String): Boolean {
            return when (mode) {
                EvalMode.EMPTY -> s == ""
                EvalMode.WHITESPACE -> s.all { c -> c == ' ' }
            }
        }
    }
}

enum class EvalMode {
    EMPTY,
    WHITESPACE
}