package com.example.trackingmypantry

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.lib.BarcodeAnalyzer
import com.example.trackingmypantry.lib.PermissionEvaluer
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.adapters.ScannedBarcodesAdapter
import com.google.mlkit.vision.barcode.Barcode
import kotlinx.android.synthetic.main.activity_camera.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarcodeScannerActivity : CameraActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var barcodesText: TextView
    private lateinit var selectButton: AppCompatButton
    private lateinit var retryButton: AppCompatButton
    private lateinit var homeButton: AppCompatImageButton
    private lateinit var stopButton: AppCompatButton
    private lateinit var cameraExecutor: ExecutorService

    private var barcodes = mutableListOf<Barcode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentCamera()

        if (PermissionEvaluer.got(this, Manifest.permission.CAMERA)) {
            this.startCamera()
        } else {
            PermissionEvaluer.request(this, Manifest.permission.CAMERA, PERMISSION_REQUEST_CODE)
        }
    }

    private fun setContentBarcodes() {
        this.setContentView(R.layout.select_barcode)

        this.recyclerView = this.findViewById(R.id.scannedBarcodesRecView)
        this.barcodesText = this.findViewById(R.id.scannedBarcodesDescText)
        this.selectButton = this.findViewById(R.id.selectBarcodeButton)
        this.retryButton = this.findViewById(R.id.retryScanButton)
        this.homeButton = this.findViewById(R.id.homeButton)

        this.recyclerView.layoutManager = LinearLayoutManager(this)

        if (this.barcodes.isNotEmpty()) {
            val list = mutableListOf<String>()
            for (barcode in barcodes) {
                if (barcode.rawValue != null) {
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

        this.selectButton.setOnClickListener {
            var checkedBarcode = (this.recyclerView.adapter as ScannedBarcodesAdapter).getCheckedBarcode()

            if (checkedBarcode != null) {
                var intent = Intent()
                intent.putExtra("barcode", checkedBarcode)
                this.setResult(RESULT_OK, intent)
                this.finish()
            } else {
                Utils.toastShow(this, "No barcode selected")
            }
        }

        this.retryButton.setOnClickListener {
            this.setContentCamera()
        }

        this.homeButton.setOnClickListener {
            this.setResult(RESULT_CANCELED, Intent())
            this.finish()
        }
    }

    private fun setContentCamera() {
        this.setContentView(R.layout.activity_camera)
        this.stopButton = findViewById(R.id.stopButton)

        this.stopButton.setOnClickListener {
            Log.e("NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN", "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")
            this.setContentBarcodes()
        }

        this.cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(this.viewFinder.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                val imageAnalysis = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(this.cameraExecutor, BarcodeAnalyzer({ scannered ->
                            if (scannered != null) {
                                this.barcodes.addAll(scannered)
                            }
                        },
                        {
                            this.setContentBarcodes()
                        }))
                    }
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch(exception: Exception) {
                this.setContentBarcodes()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        this.cameraExecutor.shutdown()
    }
}