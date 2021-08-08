package com.example.trackingmypantry

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity(private val imageCapturing: Boolean) : AppCompatActivity() {
    protected lateinit var takePhotoButton: AppCompatButton
    protected lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_camera)

        this.takePhotoButton = this.findViewById(R.id.takePhotoButton)

        if (!imageCapturing) {
            this.takePhotoButton.visibility = android.view.View.GONE
        }
        this.takePhotoButton.setOnClickListener {
            //TODO: call `takePhoto()`
        }

        this.cameraExecutor = Executors.newSingleThreadExecutor()
    }

    protected fun cameraPermissionGranted(): Boolean {
        return this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        this.cameraExecutor.shutdown()
    }
}