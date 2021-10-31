package com.example.trackingmypantry.lib.connectivity.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import java.io.IOException

class SendThread(
    private val handler: Handler,
    private val socket: BluetoothSocket,
    private val bytes: ByteArray
): Thread() {
    private val stream = socket.outputStream

    override fun run() {
        try {
            this.stream.write(bytes)

            val msg = handler.obtainMessage(MessageType.WRITE_DATA, this)
            msg.sendToTarget()
        } catch (exception: IOException) {
            Log.e("Socket bt write error", exception.message ?: "Cannot send data")

            val msg = handler.obtainMessage(MessageType.ERROR_WRITE, this)
            msg.sendToTarget()
            return
        }
    }

    fun cancel() {
        try {
            this.socket.close()
        } catch (exception: IOException) {
            Log.e("Socket bt close error", "Cannot close socket")
        }
    }
}