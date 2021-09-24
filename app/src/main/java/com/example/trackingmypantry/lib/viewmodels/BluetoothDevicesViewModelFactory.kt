package com.example.trackingmypantry.lib.viewmodels

import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BluetoothDevicesViewModelFactory(val adapter: BluetoothAdapter): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(BluetoothAdapter::class.java)
            .newInstance(adapter)
    }
}