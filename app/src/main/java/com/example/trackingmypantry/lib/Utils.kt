package com.example.trackingmypantry.lib

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.db.entities.ItemSuggestion
import com.example.trackingmypantry.db.entities.Place
import com.example.trackingmypantry.db.entities.PlaceSuggestion
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Utils {
    companion object {
        /**
         * The actual size of Int in Bytes used to convert Int to ByteArray and viceversa.
         * Same for Double
         */
        private const val INT_SIZE = Int.SIZE_BYTES
        private const val DOUBLE_SIZE = Double.SIZE_BYTES

        /**
         * Bytes used to tell the type of data encoded in a ByteArray.
         */
        private const val ITEM_ENCODING: Byte = 0
        private const val PLACE_ENCODING: Byte = 1

        enum class FollowingDataType {
            ITEM_TYPE,
            PLACE_TYPE
        }

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
            return ByteBuffer.allocate(INT_SIZE).order(ByteOrder.nativeOrder()).putInt(n).array()
            /*val buffer = ByteArray(INT_SIZE)

            for (i in 0 until INT_SIZE) {
                buffer[i] = (n shr (i * 8)).toByte()
            }

            return buffer*/
        }

        private fun byteArrayToInt(array: ByteArray): Int {
            return ByteBuffer.wrap(array).order(ByteOrder.nativeOrder()).int
            /*var res = 0

            if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN)
            for (i in 0 until INT_SIZE) {
                res = res or (array[i].toInt() shl ((INT_SIZE - i) * 8))
            }

            return res*/
        }

        private fun doubleToByteArray(d: Double): ByteArray {
            return ByteBuffer.allocate(DOUBLE_SIZE).order(ByteOrder.nativeOrder()).putDouble(d).array()
            /*val res = ByteArray(DOUBLE_SIZE)
            ByteBuffer.wrap(res).putDouble(d)
            return res*/
        }

        private fun byteArrayToDouble(array: ByteArray): Double {
            return ByteBuffer.wrap(array).order(ByteOrder.nativeOrder()).double
            /*return ByteBuffer.wrap(array).double*/
        }

        private fun concatByteArrays(arrays: Array<ByteArray>): ByteArray {
            var res = arrays[0]

            for (i in 1 until arrays.size) {
                res += arrays[i]
            }

            return res
        }

        fun itemToByteArray(item: Item, username: String): ByteArray {
            val encType = ByteArray(1)
            encType[0] = ITEM_ENCODING

            val encBarcode = item.barcode.toByteArray(Charsets.UTF_8)
            val lenBarcode = intToByteArray(encBarcode.size)

            val encName = item.name.toByteArray(Charsets.UTF_8)
            val lenName = intToByteArray(encName.size)

            val encDesc = item.description.toByteArray(Charsets.UTF_8)
            val lenDesc = intToByteArray(encDesc.size)

            val encUser = username.toByteArray(Charsets.UTF_8)
            val lenUser = intToByteArray(encUser.size)

            return if (item.image != null) {
                val encImage = item.image.toByteArray(Charsets.UTF_8)
                val lenImage = intToByteArray(encImage.size)

                concatByteArrays(arrayOf(encType, lenBarcode, encBarcode, lenName, encName, lenDesc,
                    encDesc, lenUser, encUser, lenImage, encImage))
            } else {
                concatByteArrays(arrayOf(encType, lenBarcode, encBarcode, lenName, encName, lenDesc,
                    encDesc, lenUser, encUser))
            }
        }

        fun byteArrayToItemSuggestion(array: ByteArray): ItemSuggestion {
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

            oldCursor = cursor
            cursor += INT_SIZE

            val lenUser = byteArrayToInt(array.sliceArray(IntRange(oldCursor, cursor - 1)))

            oldCursor = cursor
            cursor += lenUser

            val user = array.decodeToString(oldCursor, cursor)

            if (array.size == cursor) {
                return ItemSuggestion(barcode, name, desc, null, user)
            }

            oldCursor = cursor
            cursor += INT_SIZE

            val lenImage = byteArrayToInt(array.sliceArray(IntRange(oldCursor, cursor - 1)))

            oldCursor = cursor
            cursor += lenImage

            val image = array.decodeToString(oldCursor, cursor)

            return ItemSuggestion(barcode, name, desc, image, user)
        }

        fun placeToByteArray(place: Place, username: String): ByteArray {
            val encType = ByteArray(1)
            encType[0] = PLACE_ENCODING

            val encLatitude = doubleToByteArray(place.latitude)
            val encLongitude = doubleToByteArray(place.longitude)

            val encTitle = place.title.toByteArray(Charsets.UTF_8)
            val lenTitle = intToByteArray(encTitle.size)

            val encUser = username.toByteArray(Charsets.UTF_8)
            val lenUser = intToByteArray(encUser.size)

            return concatByteArrays(arrayOf(encType, encLatitude, encLongitude, lenTitle, encTitle,
                lenUser, encUser))
        }

        fun byteArrayToPlaceSuggestion(array: ByteArray): PlaceSuggestion {
            var oldCursor = 0
            var cursor = DOUBLE_SIZE

            val latitude = byteArrayToDouble(array.sliceArray(IntRange(oldCursor, cursor - 1)))

            oldCursor = cursor
            cursor += DOUBLE_SIZE

            val longitude = byteArrayToDouble(array.sliceArray(IntRange(oldCursor, cursor - 1)))

            oldCursor = cursor
            cursor += INT_SIZE

            val lenTitle = byteArrayToInt(array.sliceArray(IntRange(oldCursor, cursor - 1)))

            oldCursor = cursor
            cursor += lenTitle

            val title = array.decodeToString(oldCursor, cursor)

            oldCursor = cursor
            cursor += INT_SIZE

            val lenUser = byteArrayToInt(array.sliceArray(IntRange(oldCursor, cursor - 1)))

            oldCursor = cursor
            cursor += lenUser

            val user = array.decodeToString(oldCursor, cursor)

            return PlaceSuggestion(latitude, longitude, title, user)
        }

        /**
         * It returns the array without the first byte, that is the byte which tells what
         * type of data follows in the array.
         */
        fun payloadOf(array: ByteArray): ByteArray {
            return array.sliceArray(IntRange(1, array.size - 1))
        }

        /**
         * It returns the type of the data encoded in the array.
         */
        fun encodedTypeOf(array: ByteArray): FollowingDataType {
            return when (array[0]) {
                ITEM_ENCODING -> FollowingDataType.ITEM_TYPE
                PLACE_ENCODING -> FollowingDataType.PLACE_TYPE
                else -> throw RuntimeException()
            }
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