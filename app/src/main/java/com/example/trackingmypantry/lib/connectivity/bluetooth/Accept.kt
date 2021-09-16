package com.example.trackingmypantry.lib.connectivity.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import java.io.IOException

/**
 * Thread for the raw bluetooth accept(). The returning socket is sent through a message to
 * the thread that passed @param `handler`.
 */
class Accept(
    adapter: BluetoothAdapter,
    private val handler: Handler): Thread() {

    private val SERVICE_NAME = "trmypa_service_name"
    private val welcomingSocket: BluetoothServerSocket by lazy(LazyThreadSafetyMode.NONE) {
        adapter.listenUsingRfcommWithServiceRecord(SERVICE_NAME, APP_UUID)
    }

    override fun run() {
        var loop = true
        while (loop) {
            val socket: BluetoothSocket? = try {
                this.welcomingSocket.accept()
            } catch (exception: IOException) {
                Log.e("Socket bt accept error", exception.message ?: "Cannot accept connections")
                loop = false
                null
            }

            socket.also {
                val msg = handler.obtainMessage(MessageType.ACCEPT_SOCKET, it)
                msg.sendToTarget()
                this.welcomingSocket.close()
                loop = false
            }
        }
    }

    fun cancel() {
        try {
            this.welcomingSocket.close()
        } catch (exception: IOException) {
            Log.e("Socket bt close error", exception.message ?: "Cannot close connections")
        }
    }
}