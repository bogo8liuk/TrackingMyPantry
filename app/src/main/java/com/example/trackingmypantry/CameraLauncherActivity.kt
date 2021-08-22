package com.example.trackingmypantry

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts

open class CameraLauncherActivity : AppCompatActivity() {
    val IMAGE_CAPTURE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val cameraLauncher = { success: (Bitmap) -> Unit, error: () -> Unit ->
        this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    success(result.data!!.extras!!["data"] as Bitmap)
                }

                else -> {
                    error()
                }
            }
        }
    }

    protected fun launch(success: (Bitmap) -> Unit, error: () -> Unit) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        this.cameraLauncher(success, error).launch(intent)
    }
}