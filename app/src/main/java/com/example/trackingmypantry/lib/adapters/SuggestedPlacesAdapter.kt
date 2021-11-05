package com.example.trackingmypantry.lib.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.db.entities.PlaceSuggestion

class SuggestedPlacesAdapter(
    private val deleteCallback: IndexedArrayCallback<PlaceSuggestion>,
    private val sendCallback: IndexedArrayCallback<PlaceSuggestion>,
    private val places: Array<PlaceSuggestion>
): RecyclerView.Adapter<SuggestedPlacesAdapter.ViewHolder>() {
    private var isExpanded = BooleanArray(places.size) { _ -> false }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameButton: AppCompatButton = view.findViewById(R.id.suggestedPlaceNameButton)
        val nameExpandedButton: AppCompatButton = view.findViewById(R.id.suggestedPlaceNameExpandedButton)
        val userText: TextView = view.findViewById(R.id.suggestedPlaceUser)
        val layout: LinearLayout = view.findViewById(R.id.handleSuggestionLayout)
        val deleteButton: AppCompatButton = layout.findViewById(R.id.removeSuggestedPlaceButton)
        val sendButton: AppCompatButton = layout.findViewById(R.id.sendSuggestedPlaceButton)

        init {
            this.deleteButton.setOnClickListener {
                deleteCallback(IndexedArray(places, this.adapterPosition))
            }

            this.sendButton.setOnClickListener {
                sendCallback(IndexedArray(places, this.adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.suggested_place_row,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameButton.text = places[position].title
        holder.nameExpandedButton.text = places[position].title
        holder.userText.text = places[position].username

        if (this.isExpanded[position]) {
            holder.nameButton.visibility = android.view.View.GONE
            holder.nameExpandedButton.visibility = android.view.View.VISIBLE
            holder.layout.visibility = android.view.View.VISIBLE
        } else {
            holder.nameButton.visibility = android.view.View.VISIBLE
            holder.nameExpandedButton.visibility = android.view.View.GONE
            holder.layout.visibility = android.view.View.GONE
        }

        holder.nameButton.setOnClickListener {
            this.isExpanded[position] = true
            this.notifyItemChanged(position)
        }

        holder.nameExpandedButton.setOnClickListener {
            this.isExpanded[position] = false
            this.notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return places.size
    }
}