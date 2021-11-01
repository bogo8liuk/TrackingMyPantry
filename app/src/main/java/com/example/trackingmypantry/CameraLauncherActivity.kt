package com.example.trackingmypantry

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.example.trackingmypantry.lib.PermissionEvaluer

open class CameraLauncherActivity : AppCompatActivity() {
    companion object {
        const val BITMAP_EXTRA = "data"
    }

    protected var encodedImage: Bitmap? = null
        private set

    private lateinit var successCallback: (Bitmap) -> Unit
    private lateinit var errorCallback: () -> Unit

    private val cameraLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    val bitmap = result.data!!.extras!![BITMAP_EXTRA] as Bitmap
                    this.encodedImage = bitmap
                    this.successCallback(bitmap)
                }

                else -> {
                    this.errorCallback()
                }
            }
        }

    protected fun cameraLaunch(success: (Bitmap) -> Unit, error: () -> Unit) {
        this.successCallback = success
        this.errorCallback = error

        if (PermissionEvaluer.got(this, Manifest.permission.CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            this.cameraLauncher.launch(intent)
        } else {
            PermissionEvaluer.request(this, Manifest.permission.CAMERA)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            /* At this point, the two callbacks are already been set, because the request for camera
             * permissions is carried out only in cameraLaunch() method that set the two callbacks.
             */
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            this.cameraLauncher.launch(intent)
        }
    }
}