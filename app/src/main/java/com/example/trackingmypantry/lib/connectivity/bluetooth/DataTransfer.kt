package com.example.trackingmypantry.lib.connectivity.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Handler
import java.io.IOException
import java.io.InputStream

class DataTransfer(private val handler: Handler) {
    data class IncomingData(
        val numBytes: Int,
        val data: ByteArray
    )

    inner class Receive(socket: BluetoothSocket): Thread() {
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

            val msg = handler.obtainMessage(MessageType.READ_DATA, IncomingData(readBytes, this.buffer))
            msg.sendToTarget()
        }
    }

    inner class Send(socket: BluetoothSocket, bytes: ByteArray): Thread() {
        private val stream = socket.outputStream

    }
}