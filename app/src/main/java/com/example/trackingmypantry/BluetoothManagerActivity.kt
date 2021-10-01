package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.connectivity.bluetooth.AcceptThread
import com.example.trackingmypantry.lib.connectivity.bluetooth.ConnectThread
import com.example.trackingmypantry.lib.connectivity.bluetooth.MessageType

class BluetoothManagerActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var devicesDescText: TextView
    private lateinit var pairButton: AppCompatButton
    private lateinit var acceptButton: AppCompatButton

    private lateinit var connectThread: ConnectThread
    private lateinit var acceptThread: AcceptThread

    private val btInfoHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MessageType.START_CONNECT -> {
                    connectThread = msg.obj as ConnectThread
                }

                MessageType.START_ACCEPT -> {
                    acceptThread = msg.obj as AcceptThread
                }

                MessageType.CONNECTED -> {
                    connectThread.cancel()
                    //TODO: state connected, start ShareActivity
                }

                MessageType.ACCEPTED -> {
                    acceptThread.cancel()
                    //TODO: state connected, start ShareActivity
                }

                MessageType.ERROR_CONNECT -> {
                    Utils.toastShow(this@BluetoothManagerActivity, "Could not connect to this device")
                }

                MessageType.ERROR_ACCEPT -> {
                    Utils.toastShow(this@BluetoothManagerActivity, "Could not connect to other devices")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_bluetooth_manager)

        this.recyclerView = this.findViewById(R.id.btDevicesRecView)
        this.devicesDescText = this.findViewById(R.id.devicesDescText)
        this.pairButton = this.findViewById(R.id.pairButton)
        this.acceptButton = this.findViewById(R.id.acceptButton)
    }
}