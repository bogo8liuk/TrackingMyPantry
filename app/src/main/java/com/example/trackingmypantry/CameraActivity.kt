package com.example.trackingmypantry

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.example.trackingmypantry.lib.PermissionEvaluer
import com.example.trackingmypantry.lib.ResultCode
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

abstract class CameraActivity() : AppCompatActivity() {
    protected val PERMISSION_REQUEST_CODE = 1

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.startCamera()
                }
            }
        }
    }

    protected abstract fun startCamera()
}