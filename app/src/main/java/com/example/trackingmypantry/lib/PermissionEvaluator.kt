package com.example.trackingmypantry.lib

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class PermissionEvaluator {
    companion object {
        const val DEFAULT_REQUEST_CODE = 0

        fun got(context: Context, permission: String): Boolean {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }

        fun request(activity: Activity, permission: String, requestCode: Int = DEFAULT_REQUEST_CODE) {
            /*
            * If app is running Android 12 or higher, then ACCESS_FINE_LOCATION must be requested
            * along with ACCESS_COARSE_LOCATION in a single request.
            */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                permission == android.Manifest.permission.ACCESS_FINE_LOCATION) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        permission
                    ),
                    requestCode
                )
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
            }
        }
    }
}