package com.example.trackingmypantry.lib.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val radioButton: RadioButton = view.findViewById<RadioButton>(R.id.locationButton)

        init {
            this.radioButton.setOnClickListener {
                it as RadioButton
                if (it.isChecked) {
                    onUncheck(locations[this.adapterPosition])
                } else {
                    onCheck(locations[this.adapterPosition])
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
        holder.radioButton.text = locations[position].title
    }

    override fun getItemCount(): Int {
        return locations.size
    }
}