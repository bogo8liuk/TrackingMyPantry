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
    context: Context,
    private val resource: Int,
    private val deviceClickCallback: IndexedArrayCallback<BluetoothDevice>,
    private val devices: ArrayList<BluetoothDevice>
): ArrayAdapter<BluetoothDevice>(context, resource, devices) {
    //): RecyclerView.Adapter<BluetoothDevicesAdapter.ViewHolder>() {

    private fun initView(view: View, position: Int): View {
        val deviceButton: AppCompatButton = view.findViewById(R.id.deviceButton)

        deviceButton.text = this.devices[position].name
        deviceButton.setOnClickListener {
            deviceClickCallback(IndexedArray(devices.toTypedArray(), position))
        }

        return view
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return convertView ?: this.initView(
            inflater.inflate(resource, parent, false),
            position
        )
    }
}