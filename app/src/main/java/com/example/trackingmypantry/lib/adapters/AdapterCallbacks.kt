package com.example.trackingmypantry.lib.adapters

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Handler
import com.example.trackingmypantry.db.entities.Collection

data class IndexedArray<T>(
    val array: Array<T>,
    val index: Int
)

typealias BluetoothAdapterCallback = (
    IndexedArray<BluetoothDevice>,
    BluetoothAdapter,
    Handler
) -> Unit

typealias CollectionsAdapterCallback = (
    IndexedArray<Collection>
) -> Unit