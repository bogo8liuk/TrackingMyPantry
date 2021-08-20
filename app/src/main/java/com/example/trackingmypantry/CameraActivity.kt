package com.example.trackingmypantry

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.example.trackingmypantry.lib.ResultCode
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

abstract class CameraActivity() : AppCompatActivity() {
    protected lateinit var takePhotoButton: AppCompatButton
    protected lateinit var cameraExecutor: ExecutorService
    protected val PERMISSION_REQUEST_CODE = 1

    protected fun setContentCamera() {
        this.setContentView(R.layout.activity_camera)

        this.takePhotoButton = this.findViewById(R.id.takePhotoButton)

        val extras = this.intent.extras
        if (!(extras!!["imageCapturing"] as Boolean)) {
            this.takePhotoButton.visibility = android.view.View.GONE
        }
        this.takePhotoButton.setOnClickListener {
            //TODO: call `takePhoto()`
        }

        this.cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentCamera()

        if (cameraPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    this.setResult(ResultCode.DENIED_PERMISSIONS, Intent())
                    this.finish()
                }
            }
        }
    }

    protected fun cameraPermissionGranted(): Boolean {
        return this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    protected abstract fun startCamera()

    override fun onDestroy() {
        super.onDestroy()
        this.cameraExecutor.shutdown()
    }
}