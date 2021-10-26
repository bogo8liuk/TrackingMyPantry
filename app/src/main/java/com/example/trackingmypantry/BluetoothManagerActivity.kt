package com.example.trackingmypantry

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.companion.BluetoothDeviceFilter
import android.content.BroadcastReceiver
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.adapters.BluetoothDevicesAdapter
import com.example.trackingmypantry.lib.adapters.IndexedArrayCallback
import com.example.trackingmypantry.lib.connectivity.bluetooth.AcceptThread
import com.example.trackingmypantry.lib.connectivity.bluetooth.BlueUtils
import com.example.trackingmypantry.lib.connectivity.bluetooth.ConnectThread
import com.example.trackingmypantry.lib.connectivity.bluetooth.MessageType
import com.example.trackingmypantry.lib.viewmodels.BluetoothDevicesViewModel
import com.example.trackingmypantry.lib.viewmodels.BluetoothDevicesViewModelFactory

class BluetoothManagerActivity : AppCompatActivity() {
    companion object {
        private const val BLUETOOTH_ACCEPTED_SOCKET_KEY = 0
        private const val BLUETOOTH_CONNECTED_SOCKET_KEY = 1
        private const val BLUETOOTH_CONNECT_THREAD_KEY = 2
        private const val BLUETOOTH_ACCEPT_THREAD_KEY = 3
    }

    private lateinit var btAdapter: BluetoothAdapter
    private lateinit var receiver: BroadcastReceiver

    private val btInfoHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MessageType.START_CONNECT -> {
                    val connectThread = msg.obj as ConnectThread
                    Utils.saveValue(BLUETOOTH_CONNECT_THREAD_KEY, connectThread)
                }

                MessageType.START_ACCEPT -> {
                    val acceptThread = msg.obj as AcceptThread
                    Utils.saveValue(BLUETOOTH_ACCEPT_THREAD_KEY, acceptThread)
                }

                MessageType.CONNECTED -> {
                    val socket = msg.obj as BluetoothSocket
                    Utils.saveValue(BLUETOOTH_CONNECTED_SOCKET_KEY, socket)

                    val intent = Intent(this@BluetoothManagerActivity, ShareActivity::class.java)
                    intent.putExtra(
                        ShareActivity.BLUETOOTH_SOCKET_EXTRA,
                        BLUETOOTH_CONNECTED_SOCKET_KEY
                    )
                    intent.putExtra(
                        ShareActivity.BLUETOOTH_THREAD_EXTRA,
                        BLUETOOTH_CONNECT_THREAD_KEY
                    )
                    intent.putExtra(ShareActivity.BLUETOOTH_USERNAME_EXTRA, btAdapter.name)
                    this@BluetoothManagerActivity.startActivity(intent)
                }

                MessageType.ACCEPTED -> {
                    val socket = msg.obj as BluetoothSocket
                    Utils.saveValue(BLUETOOTH_ACCEPTED_SOCKET_KEY, socket)

                    val intent = Intent(this@BluetoothManagerActivity, AcceptSuggestionsActivity::class.java)
                    intent.putExtra(
                        AcceptSuggestionsActivity.BLUETOOTH_SOCKET_EXTRA,
                        BLUETOOTH_ACCEPTED_SOCKET_KEY
                    )
                    intent.putExtra(
                        AcceptSuggestionsActivity.BLUETOOTH_THREAD_EXTRA,
                        BLUETOOTH_ACCEPT_THREAD_KEY
                    )
                    this@BluetoothManagerActivity.startActivity(intent)
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

    private val enablingLauncher = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                // Success: do nothing, the user can continue using this functionality
            }

            RESULT_CANCELED -> {
                Utils.toastShow(this, "Enable bluetooth to use this functionality")
                this.finish()
            }
        }
    }

    private val makeDiscoverableLauncher = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                AlertDialog.Builder(this)
                    .setMessage("If someone finds your device, please click " +
                        "the button for accepting requests")
                    .setPositiveButton(R.string.positiveOk, null)
                    .show()
            }

            RESULT_CANCELED -> {
                Utils.toastShow(this, "Your device has to be discoverable to be paired")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.btAdapter = BlueUtils.bluetoothAdapter(this)
        BlueUtils.enableRequestIfDisabled(this.btAdapter, this.enablingLauncher)

        this.receiver = BlueUtils.bluetoothDeviceReceiver(this.onFoundDevice)
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        this.registerReceiver(this.receiver, filter)

        this.setContentView(R.layout.activity_bluetooth_manager)

        val recyclerView: RecyclerView = this.findViewById(R.id.btDevicesRecView)
        val devicesDescText: TextView = this.findViewById(R.id.devicesDescText)
        val discoveryButton: AppCompatButton = this.findViewById(R.id.discoveryButton)
        val acceptButton: AppCompatButton = this.findViewById(R.id.acceptButton)

        recyclerView.adapter = BluetoothDevicesAdapter(this.connect, arrayOf<BluetoothDevice>())
        recyclerView.layoutManager = LinearLayoutManager(this)

        acceptButton.setOnClickListener {
            AcceptThread(btAdapter, btInfoHandler).run()
        }

        discoveryButton.setOnClickListener {
            val actionButtons = RadioGroup(this)
            val findButton = RadioButton(this)
            val makeDiscoverableButton = RadioButton(this)

            findButton.text = "Find devices"
            makeDiscoverableButton.text = "Make others to find you"

            actionButtons.addView(findButton)
            actionButtons.addView(makeDiscoverableButton)

            AlertDialog.Builder(this)
                .setTitle("Choose action")
                .setMessage("Choose one of the following action")
                .setView(actionButtons)
                .setNegativeButton(R.string.negativeCanc, null)
                .setPositiveButton(R.string.choose, DialogInterface.OnClickListener { _, _ ->
                    if (findButton.isChecked) {
                        val success = this.btAdapter.startDiscovery()
                        if (success) {
                            Utils.toastShow(this, "It may take a few seconds")
                        } else {
                            Utils.toastShow(this, "Cannot find other devices")
                        }
                    } else if (makeDiscoverableButton.isChecked) {
                        BlueUtils.makeDiscoverable(this.makeDiscoverableLauncher)
                    }
                })
                .show()
        }

        val model: BluetoothDevicesViewModel by viewModels {
            BluetoothDevicesViewModelFactory(btAdapter)
        }
        model.getDevices().observe(this, Observer<List<BluetoothDevice>> {
            if (it.isEmpty()) {
                devicesDescText.text = "No paired devices"
            } else {
                val adapter = BluetoothDevicesAdapter(this.connect, it.toTypedArray())
                recyclerView.adapter = adapter
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(this.receiver)
    }

    private val connect: IndexedArrayCallback<BluetoothDevice> = {
        ConnectThread(this.btAdapter, it.array[it.index], this.btInfoHandler).run()
    }

    private val onFoundDevice = { device: BluetoothDevice? ->
        if (device != null) {
            Utils.toastShow(this, "${device.name} found!")

            ConnectThread(this.btAdapter, device, this.btInfoHandler).run()
        }
    }
}