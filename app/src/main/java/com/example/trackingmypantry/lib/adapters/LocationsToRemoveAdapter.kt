package com.example.trackingmypantry.lib.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.db.entities.Place

/**
 * @warning The viewholder inside this adapter contains a radio button;
 * by clicking the radio button, one of the two callbacks (@param `onCheck`
 * or @param `onUncheck`) is called, so the passed callbacks must not be
 * time-consuming computations, in order not to block UI thread.
 */
class LocationsToRemoveAdapter(
    private val locations: Array<Place>,
    private val onCheck: (Place) -> Unit,
    private val onUncheck: (Place) -> Unit):
    RecyclerView.Adapter<LocationsToRemoveAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.locationCheckBox)

        init {
            this.checkBox.setOnClickListener {
                it as CheckBox
                if (it.isChecked) {
                    onCheck(locations[this.adapterPosition])
                } else {
                    onUncheck(locations[this.adapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.location_to_remove_row,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkBox.text = locations[position].title
    }

    override fun getItemCount(): Int {
        return locations.size
    }
}