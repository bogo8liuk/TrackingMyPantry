package com.example.trackingmypantry.lib.connectivity.bluetooth

import android.content.Context
import android.content.pm.PackageManager

class Availability {
    companion object {
        fun bluetoothClassic(context: Context): Boolean {
            return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
        }
    }
}