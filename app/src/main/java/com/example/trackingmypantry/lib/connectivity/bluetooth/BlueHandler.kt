package com.example.trackingmypantry.lib.connectivity.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class BlueHandler {
    companion object {
        const val BLUETOOTH_ENABLING_REQUEST_CODE = 0
        const val DISCOVERABLE_REQUEST_CODE = 1

        /**
         * It returns true if classic bluetooth is supported, false otherwise
         */
        fun hasBluetoothFeature(context: Context): Boolean {
            return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
        }

        /**
         * It returns a bluetooth adapter.
         */
        fun bluetoothAdapter(context: Context): BluetoothAdapter {
            return (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        }

        /**
         * It carries out a request of bluetooth enabling if bluetooth is not enabled.
         * @warning It calls `startActivityForResult`, so the request needs to be handled
         * by implementing `onActivityResult`.
         */
        fun enableRequestIfDisabled(activity: Activity, adapter: BluetoothAdapter) {
            if (!adapter.isEnabled) {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                activity.startActivityForResult(intent, BLUETOOTH_ENABLING_REQUEST_CODE)
            }
        }

        /**
         * It scans all @param `devices` until it finds the one that makes @param
         * `match` returning true and it calls @param `callback` on it.
         * If @param `continueAfterMatch` is true, it continues the scan even after
         * the match.
         */
        fun queryBondedDevices(
            devices: Set<BluetoothDevice>,
            match: (BluetoothDevice) -> Boolean,
            callback: (BluetoothDevice) -> Unit,
            continueAfterMatch: Boolean
        ) {
            devices.forEach { device ->
                val matched = match(device)
                if (matched) {
                    callback(device)

                    if (!continueAfterMatch) {
                        return
                    }
                }
            }
        }

        /**
         * It carries out a request to make the current device discoverable by other
         * bluetooth devices for 5 minutes.
         * @warning It calls `startActivityForResult`, so the request needs to be handled
         * by implementing `onActivityResult`.
         */
        fun makeDiscoverable(activity: Activity) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                this.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            }
            activity.startActivityForResult(intent, DISCOVERABLE_REQUEST_CODE)
        }

        /**
         * Functions that wraps `startDiscovery()` of `BluetoothAdpater`.
         */
        fun startDiscovery(adapter: BluetoothAdapter): Boolean {
            return adapter.startDiscovery()
        }

        /**
         * It returns a `BroadcastReceiver` object that handles the case `stratDiscovery()`
         * finds a device.
         * @warning The receiver must unregistered.
         */
        fun bluetoothDeviceReceiver(handler: (BluetoothDevice?) -> Unit) = object: BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        handler(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE))
                    }
                }
            }
        }

        /**
         * Given an adapter, it accepts bluetooth connections and it executes the callback
         * whenever a connection is established.
         * @warning It may fails and not connect.
         */
        fun accept(adapter: BluetoothAdapter, callback: (BluetoothSocket?) -> Unit) {
            Accept(adapter) {
                callback(it)
            }.run()
        }

        /**
         * Given an adapter and a device, it continues trying to connect to the device and
         * it executes whenever a connection is established.
         * @warning It may fails and not connect.
         */
        fun connect(adapter: BluetoothAdapter, device: BluetoothDevice, callback: (BluetoothSocket) -> Unit) {
            Connect(adapter, device) {
                callback(it)
            }.run()
        }
    }
}