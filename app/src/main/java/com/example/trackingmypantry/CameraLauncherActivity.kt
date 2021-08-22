package com.example.trackingmypantry

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts

open class CameraLauncherActivity : AppCompatActivity() {
    protected var encodedImage: Bitmap? = null
        private set

    private lateinit var successCallback: (Bitmap) -> Unit
    private lateinit var errorCallback: () -> Unit

    private val cameraLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    val bitmap = result.data!!.extras!!["data"] as Bitmap
                    this.encodedImage = bitmap
                    this.successCallback(bitmap)
                }

                else -> {
                    this.errorCallback()
                }
            }
        }

    protected fun cameraLaunch(success: (Bitmap) -> Unit, error: () -> Unit) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        this.successCallback = success
        this.errorCallback = error
        this.cameraLauncher.launch(intent)
    }
}