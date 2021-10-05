package com.example.trackingmypantry.db

import androidx.room.TypeConverter
import java.util.*

class Converter {
    @TypeConverter
    fun longToDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun stringToDouble(value: String?): Double? {
        return value?.toDouble()
    }

    @TypeConverter
    fun doubleToString(value: Double?): String? {
        return value?.toString()
    }
}