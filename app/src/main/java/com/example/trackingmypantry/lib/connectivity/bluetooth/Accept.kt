package com.example.trackingmypantry.lib.connectivity.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException

class Accept(
    private val adapter: BluetoothAdapter,
    private val handleSocket: (BluetoothSocket?) -> Unit): Thread() {
    private val SERVICE_NAME = "tmp_service_name"
    private val welcomingSocket: BluetoothServerSocket by lazy(LazyThreadSafetyMode.NONE) {
        adapter.listenUsingRfcommWithServiceRecord(SERVICE_NAME, APP_UUID)
    }

    override fun run() {
        var loop = true
        while (loop) {
            val socket: BluetoothSocket? = try {
                this.welcomingSocket.accept()
            } catch (exception: IOException) {
                Log.e("Socket accept error", exception.message ?: "Cannot accept connections")
                loop = false
                null
            }

            socket.also {
                this.handleSocket(it)
                this.welcomingSocket.close()
                loop = false
            }
        }
    }

    fun cancel() {
        try {
            this.welcomingSocket.close()
        } catch (exception: IOException) {
            Log.e("Socket close error", exception.message ?: "Cannot close connections")
        }
    }
}