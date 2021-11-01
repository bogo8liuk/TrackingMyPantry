package com.example.trackingmypantry.lib.connectivity.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import java.io.IOException

/**
 * Thread for the raw bluetooth connect(). The returning socket is sent through a message to
 * the thread that passed @param `handler`.
 */
class ConnectThread(
    private val adapter: BluetoothAdapter,
    device: BluetoothDevice,
    private val handler: Handler): Thread() {

    private val connectSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        device.createRfcommSocketToServiceRecord(APP_UUID)
    }

    override fun run() {
        this.adapter.cancelDiscovery()

        this.connectSocket?.let { it ->
            try {
                val startMsg = this.handler.obtainMessage(MessageType.START_CONNECT, this)
                startMsg.sendToTarget()

                it.connect()

                val connectedMsg = this.handler.obtainMessage(MessageType.CONNECTED, it)
                connectedMsg.sendToTarget()
            } catch (exception: IOException) {
                Log.e("Socket bt connection error", exception.message ?: "Cannot call connect")

                val msg = this.handler.obtainMessage(MessageType.ERROR_CONNECT)
                msg.sendToTarget()
            }
        }
    }

    fun cancel() {
        try {
            this.connectSocket?.close()
        } catch (exception: IOException) {
            Log.e("Socket bt close error", exception.message ?: "Cannot close connections")
        }
    }
}