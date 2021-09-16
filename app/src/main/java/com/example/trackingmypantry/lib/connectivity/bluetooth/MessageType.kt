package com.example.trackingmypantry.lib.connectivity.bluetooth

class MessageType {
    companion object {
        const val ACCEPT_SOCKET = 0
        const val CONNECT_SOCKET = 1
        const val READ_DATA = 2
        const val WRITE_DATA = 3
        const val ERROR_WRITE = 4
    }
}