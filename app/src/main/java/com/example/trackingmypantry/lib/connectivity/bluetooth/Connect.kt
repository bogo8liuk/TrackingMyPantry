package com.example.trackingmypantry.lib.connectivity.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException

class Connect(
    private val adapter: BluetoothAdapter,
    device: BluetoothDevice,
    private val handleSocket: (BluetoothSocket) -> Unit): Thread() {

    private val connectSocket: BluetoothSocket by lazy(LazyThreadSafetyMode.NONE) {
        device.createRfcommSocketToServiceRecord(APP_UUID)
    }

    override fun run() {
        this.adapter.cancelDiscovery()  //TODO: putting here?

        this.connectSocket.let { it ->
            try {
                it.connect()
                this.handleSocket(it)
            } catch (exception: IOException) {
                Log.e("Socket bt connection error", exception.message ?: "Cannot call connect")
            }
        }
    }

    fun cancel() {
        try {
            this.connectSocket.close()
        } catch (exception: IOException) {
            Log.e("Socket bt close error", exception.message ?: "Cannot close connections")
        }
    }
}