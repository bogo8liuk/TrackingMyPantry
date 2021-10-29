package com.example.trackingmypantry.lib.adapters

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R

class BluetoothDevicesAdapter(
    private val deviceClickCallback: IndexedArrayCallback<BluetoothDevice>,
    private val devices: Array<BluetoothDevice>
): RecyclerView.Adapter<BluetoothDevicesAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val deviceButton = view.findViewById<AppCompatButton>(R.id.deviceButton)

        init {
            this.deviceButton.setOnClickListener {
                deviceClickCallback(IndexedArray(devices, this.adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.bluetooth_device_row,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.deviceButton.text = devices[position].name
    }

    override fun getItemCount(): Int {
        return devices.size
    }
}