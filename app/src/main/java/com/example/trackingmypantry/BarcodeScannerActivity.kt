package com.example.trackingmypantry

import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.lib.BarcodeAnalyzer
import com.example.trackingmypantry.lib.adapters.ScannedBarcodesAdapter
import com.google.mlkit.vision.barcode.Barcode
import kotlinx.android.synthetic.main.activity_camera.*

class BarcodeScannerActivity : CameraActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var barcodesText: TextView
    private lateinit var selectButton: AppCompatButton
    private lateinit var retryButton: AppCompatButton

    private fun setContentBarcodes(barcodes: List<Barcode>?) {
        this.setContentView(R.layout.select_barcode)

        this.recyclerView = this.findViewById(R.id.scannedBarcodesRecView)
        this.selectButton = this.findViewById(R.id.selectBarcodeButton)
        this.retryButton = this.findViewById(R.id.retryScanButton)

        this.recyclerView.layoutManager = LinearLayoutManager(this)

        if (barcodes != null && barcodes.isNotEmpty()) {
            lateinit var list: MutableList<String>
            for (barcode in barcodes) {
                if (barcode != null) {
                    list.add(barcode.rawValue)
                }
            }

            if (list.isNotEmpty()) {
                this.barcodesText.text = "No scanned barcodes, please retry"
            } else {
                this.barcodesText.text = "Scanned barcodes:"
            }

            this.recyclerView.adapter = ScannedBarcodesAdapter(list.toTypedArray())

        } else {
            this.barcodesText.text = "No scanned barcodes, please retry"
            this.recyclerView.adapter = ScannedBarcodesAdapter(arrayOf<String>())
        }
    }

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
                        it.setAnalyzer(cameraExecutor, BarcodeAnalyzer({ barcodes ->
                            this.setContentBarcodes(barcodes)
                        },
                        {
                            this.setContentBarcodes(null)
                        }))
                    }
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch(exception: Exception) {
                this.setContentBarcodes(null)
            }
        }, ContextCompat.getMainExecutor(this))
    }
}