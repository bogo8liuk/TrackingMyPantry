package com.example.trackingmypantry.lib.connectivity.bluetooth

class MessageType {
    companion object {
        const val CONNECTED = 0
        const val ACCEPTED = 1
        const val READ_DATA = 2
        const val WRITE_DATA = 3
        const val ERROR_CONNECT = 4
        const val ERROR_ACCEPT = 5
        const val ERROR_WRITE = 6
        const val START_CONNECT = 7
        const val START_ACCEPT = 8
    }
}