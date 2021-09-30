package com.example.trackingmypantry.lib.adapters

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R

class BluetoothDevicesAdapter {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val deviceButton = view.findViewById<AppCompatButton>(R.id.deviceButton)

        init {
            this.deviceButton.setOnClickListener {
                //TODO: try to connect
            }
        }
    }
}