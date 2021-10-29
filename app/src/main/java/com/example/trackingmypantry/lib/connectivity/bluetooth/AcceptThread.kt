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
class AcceptThread(
    adapter: BluetoothAdapter,
    private val handler: Handler): Thread() {

    private val SERVICE_NAME = "trmypa_service_name"
    private val welcomingSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
        adapter.listenUsingRfcommWithServiceRecord(SERVICE_NAME, APP_UUID)
    }

    override fun run() {
        var accepting = true
        while (accepting) {
            val socket: BluetoothSocket? = try {
                val msg = handler.obtainMessage(MessageType.START_ACCEPT, this)
                msg.sendToTarget()

                this.welcomingSocket?.accept()
            } catch (exception: IOException) {
                Log.e("Socket bt accept error", exception.message ?: "Cannot accept connections")

                val msg = handler.obtainMessage(MessageType.ERROR_ACCEPT)
                msg.sendToTarget()

                accepting = false
                null
            }

            socket?.also {
                val msg = handler.obtainMessage(MessageType.ACCEPTED, it)
                msg.sendToTarget()

                this.welcomingSocket?.close()
                accepting = false
            }
        }
    }

    fun cancel() {
        try {
            this.welcomingSocket?.close()
        } catch (exception: IOException) {
            Log.e("Socket bt close error", exception.message ?: "Cannot close connections")
        }
    }
}