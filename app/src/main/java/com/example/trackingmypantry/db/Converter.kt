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
        return date?.time?.toLong()
    }
}