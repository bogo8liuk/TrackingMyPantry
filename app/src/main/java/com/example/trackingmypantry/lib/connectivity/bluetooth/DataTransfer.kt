package com.example.trackingmypantry.lib.connectivity.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import java.io.IOException

class DataTransfer(private val handler: Handler) {
    data class IncomingData(
        val numBytes: Int,
        val data: ByteArray
    )

    inner class Receive(private val socket: BluetoothSocket): Thread() {
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

        fun cancel() {
            try {
                this.socket.close()
            } catch (exception: IOException) {
                Log.e("Socket bt close error", "Cannot close socket")
            }
        }
    }

    inner class Send(private val socket: BluetoothSocket, private val bytes: ByteArray): Thread() {
        private val stream = socket.outputStream

        override fun run() {
            try {
                this.stream.write(bytes)
            } catch (exception: IOException) {
                Log.e("Socket bt write error", exception.message ?: "Cannot send data")
                val msg = handler.obtainMessage(MessageType.ERROR_WRITE, "Cannot send data")
                msg.sendToTarget()
                return
            }

            val msg = handler.obtainMessage(MessageType.WRITE_DATA)
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
}