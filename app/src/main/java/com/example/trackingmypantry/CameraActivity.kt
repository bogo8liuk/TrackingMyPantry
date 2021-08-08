package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class CameraActivity(private val imageCapturing: Boolean) : AppCompatActivity() {
    protected lateinit var takePhotoButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_camera)

        this.takePhotoButton = this.findViewById(R.id.takePhotoButton)

        if (!imageCapturing) {
            this.takePhotoButton.visibility = android.view.View.GONE
        }
    }
}