package com.example.trackingmypantry.lib

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionEvaluer {
    companion object {
        const val DEFAULT_REQUEST_CODE = 0

        fun got(context: Context, permission: String): Boolean {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }

        fun request(activity: Activity, permission: String, requestCode: Int = DEFAULT_REQUEST_CODE) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }
}