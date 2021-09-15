package com.example.trackingmypantry.lib.connectivity.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class BlueHandler {
    companion object {
        const val DEFAULT_REQUEST_CODE = 0

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
         * It carries out a request of bluetooth connection if bluetooth is not enabled.
         * @warning It calls `startActivityForResult`, so the request needs to be handled
         * by implementing `onActivityResult`.
         */
        fun requestIfDisabled(activity: Activity, adapter: BluetoothAdapter) {
            if (!adapter.isEnabled) {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                activity.startActivityForResult(intent, DEFAULT_REQUEST_CODE)
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
    }
}