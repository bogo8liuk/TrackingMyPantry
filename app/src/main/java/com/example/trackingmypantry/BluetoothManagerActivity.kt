package com.example.trackingmypantry

import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.adapters.BluetoothDevicesAdapter
import com.example.trackingmypantry.lib.connectivity.bluetooth.AcceptThread
import com.example.trackingmypantry.lib.connectivity.bluetooth.BlueUtils
import com.example.trackingmypantry.lib.connectivity.bluetooth.ConnectThread
import com.example.trackingmypantry.lib.connectivity.bluetooth.MessageType
import com.example.trackingmypantry.lib.viewmodels.BluetoothDevicesViewModel
import com.example.trackingmypantry.lib.viewmodels.BluetoothDevicesViewModelFactory

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

        val btAdapter = BlueUtils.bluetoothAdapter(this)

        this.recyclerView.adapter = BluetoothDevicesAdapter(btAdapter, btInfoHandler, arrayOf<BluetoothDevice>())
        this.recyclerView.layoutManager = LinearLayoutManager(this)

        val model: BluetoothDevicesViewModel by viewModels {
            BluetoothDevicesViewModelFactory(btAdapter)
        }
        model.getDevices().observe(this, Observer<List<BluetoothDevice>> {
            val adapter = BluetoothDevicesAdapter(btAdapter, btInfoHandler, it.toTypedArray())
            this.recyclerView.adapter = adapter
        })
    }
}