package com.example.trackingmypantry.lib

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(private val successListener: (barcode: String) -> Unit, private val errListener: () -> Unit): ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient()

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        this.successListener(barcode.rawValue ?: "")
                    }
                }
                .addOnFailureListener {
                    this.errListener()
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}