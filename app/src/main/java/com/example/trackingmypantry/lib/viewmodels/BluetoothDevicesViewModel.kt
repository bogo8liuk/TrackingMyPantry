package com.example.trackingmypantry.lib.viewmodels

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BluetoothDevicesViewModel(adapter: BluetoothAdapter): ViewModel() {
    private val devices: MutableLiveData<List<BluetoothDevice>> by lazy {
        MutableLiveData<List<BluetoothDevice>>().also {
            it.value = adapter.bondedDevices.toList()
        }
    }

    fun getDevices(): LiveData<List<BluetoothDevice>> {
        return devices
    }
}