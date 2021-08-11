package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.trackingmypantry.lib.BarcodeAnalyzer
import kotlinx.android.synthetic.main.activity_camera.*

class BarcodeScannerActivity : CameraActivity() {
    override fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                val imageAnalysis = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, BarcodeAnalyzer({ barcode ->

                        },
                        {

                        }))
                    }
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch(exception: Exception) {
                //TODO: manage errors
            }
        }, ContextCompat.getMainExecutor(this))
    }
}