package com.example.trackingmypantry.lib

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.db.entities.ItemSuggestion
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.nio.charset.Charset

class Utils {
    companion object {
        /**
         * The actual size of Int in Bytes used to convert Int to ByteArray and viceversa.
         */
        private const val INT_SIZE = Int.SIZE_BYTES

        private val hashMap = HashMap<Int, Any>()

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

        private fun intToByteArray(n: Int): ByteArray {
            val buffer = ByteArray(INT_SIZE)

            for (i in 0 until INT_SIZE) {
                buffer[i] = (n shr (i * 8)).toByte()
            }

            return buffer
        }

        private fun byteArrayToInt(array: ByteArray): Int {
            var res = 0

            for (i in 0 until INT_SIZE) {
                res = res or (array[i].toInt() shl (i * 8))
            }

            return res
        }

        fun itemToByteArray(item: Item): ByteArray {
            val encBarcode = item.barcode.toByteArray(Charsets.UTF_8)
            val lenBarcode = intToByteArray(encBarcode.size)

            val encName = item.name.toByteArray(Charsets.UTF_8)
            val lenName = intToByteArray(encName.size)

            val encDesc = item.description.toByteArray(Charsets.UTF_8)
            val lenDesc = intToByteArray(encDesc.size)

            val concat = { arrays: Array<ByteArray> ->
                var res = arrays[0]

                for (i in 1 until arrays.size) {
                    res += arrays[i]
                }

                res
            }

            return if (item.image != null) {
                val encImage = item.image.toByteArray(Charsets.UTF_8)
                val lenImage = intToByteArray(encImage.size)

                concat(arrayOf(lenBarcode, encBarcode, lenName, encName, lenDesc, encDesc,
                    lenImage, encImage))
            } else {
                concat(arrayOf(lenBarcode, encBarcode, lenName, encName, lenDesc, encDesc))
            }
        }

        fun byteArrayToItemSuggestion(array: ByteArray): Array<String> {
            var oldCursor = 0
            var cursor = INT_SIZE

            val lenBarcode = byteArrayToInt(array.sliceArray(IntRange(oldCursor, cursor - 1)))

            oldCursor = cursor
            cursor += lenBarcode

            val barcode = array.decodeToString(oldCursor, cursor)

            oldCursor = cursor
            cursor += INT_SIZE

            val lenName = byteArrayToInt(array.sliceArray(IntRange(oldCursor, cursor - 1)))

            oldCursor = cursor
            cursor += lenName

            val name = array.decodeToString(oldCursor, cursor)

            oldCursor = cursor
            cursor += INT_SIZE

            val lenDesc = byteArrayToInt(array.sliceArray(IntRange(oldCursor, cursor - 1)))

            oldCursor = cursor
            cursor += lenDesc

            val desc = array.decodeToString(oldCursor, cursor)

            if (array.size == cursor) {
                return arrayOf(barcode, name, desc)
            }

            oldCursor = cursor
            cursor += INT_SIZE

            val lenImage = byteArrayToInt(array.sliceArray(IntRange(oldCursor, cursor - 1)))

            oldCursor = cursor
            cursor += lenImage

            val image = array.decodeToString(oldCursor, cursor)

            return arrayOf(barcode, name, desc, image)
        }

        fun stringPattern(mode: EvalMode, s: String): Boolean {
            return when (mode) {
                EvalMode.EMPTY -> s == ""
                EvalMode.WHITESPACE -> s.all { c -> c == ' ' }
            }
        }

        /**
         * It saves a value that can be retrieved later.
         */
        fun saveValue(key: Int, value: Any): Unit {
            synchronized(hashMap) {
                hashMap.put(key, value)
            }
        }

        fun getSavedValue(key: Int): Any? {
            synchronized(hashMap) {
                return hashMap[key]
            }
        }
    }
}

enum class EvalMode {
    EMPTY,
    WHITESPACE
}