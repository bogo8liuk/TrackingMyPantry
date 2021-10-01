package com.example.trackingmypantry.lib.adapters

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.lib.connectivity.bluetooth.ConnectThread

class BluetoothDevicesAdapter(
    private val adapter: BluetoothAdapter,
    private val handler: Handler,
    private val devices: Array<BluetoothDevice>
    ): RecyclerView.Adapter<BluetoothDevicesAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val deviceButton = view.findViewById<AppCompatButton>(R.id.deviceButton)

        init {
            this.deviceButton.setOnClickListener {
                ConnectThread(adapter, devices[this.adapterPosition], handler).run()
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