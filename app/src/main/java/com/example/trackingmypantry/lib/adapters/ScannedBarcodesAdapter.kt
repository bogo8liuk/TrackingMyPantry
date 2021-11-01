package com.example.trackingmypantry.lib.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R

class ScannedBarcodesAdapter(private val barcodes: Array<String>):
    RecyclerView.Adapter<ScannedBarcodesAdapter.ViewHolder>() {
    // It represents the position in the recycler view of the current checked box
    private var pos: Int = -1

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var checkBox: CheckBox = view.findViewById(R.id.barcodeCheckBox)

        init {
            this.checkBox.setOnClickListener {
                if (pos >= 0) {
                    notifyItemChanged(pos)
                }

                pos = this.adapterPosition
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.scanned_barcode_row,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkBox.text = barcodes[position]

        holder.checkBox.isChecked = false
    }

    override fun getItemCount(): Int {
        return barcodes.size
    }

    fun getCheckedBarcode(): String? {
        return if (pos > -1)
            barcodes[pos]
        else
            null
    }
}