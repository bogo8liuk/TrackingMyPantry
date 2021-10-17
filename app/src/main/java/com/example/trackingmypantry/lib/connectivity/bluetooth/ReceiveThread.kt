package com.example.trackingmypantry.lib.connectivity.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import java.io.IOException

class ReceiveThread(
    private val handler: Handler,
    private val socket: BluetoothSocket
): Thread() {
    private val stream = socket.inputStream
    private val buffer = ByteArray(2048)

    override fun run() {
        var readBytes: Int = 0

        while (true) {
            readBytes = try {
                this.stream.read(this.buffer)
            } catch(exception: IOException) {
                break
            }
        }

        val msg = handler.obtainMessage(
            MessageType.READ_DATA,
            BlueUtils.IncomingData(readBytes, this.buffer)
        )
        msg.sendToTarget()
    }

    fun cancel() {
        try {
            this.socket.close()
        } catch (exception: IOException) {
            Log.e("Socket bt close error", "Cannot close socket")
        }
    }
}