package com.example.trackingmypantry.lib.connectivity.bluetooth

class MessageType {
    companion object {
        const val CONNECTED = 0
        const val READ_DATA = 1
        const val WRITE_DATA = 2
        const val ERROR_CONNECT = 3
        const val ERROR_ACCEPT = 4
        const val ERROR_WRITE = 5
    }
}