package com.example.trackingmypantry.lib.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.db.entities.ItemSuggestion
import com.example.trackingmypantry.db.entities.PlaceSuggestion

class Suggested {
    inner class ItemsAdapter(private val items: Array<ItemSuggestion>):
    RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
        private var isExpanded = BooleanArray(items.size) { _ -> false }

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val nameButton = view.findViewById<AppCompatButton>(R.id.suggestedItemNameButton)
            val nameExpandedButton = view.findViewById<AppCompatButton>(R.id.suggestedItemNameExpandedButton)
            val barcodeText = view.findViewById<TextView>(R.id.suggestedItemBarcode)
            val descriptionText = view.findViewById<TextView>(R.id.suggestedItemDescription)
            val userText = view.findViewById<TextView>(R.id.suggestedItemUser)
            val image = view.findViewById<ImageView>(R.id.suggestedItemImage)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.suggested_item_row,
                parent,
                false
            )

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

    inner class PlacesAdapter(private val places: Array<PlaceSuggestion>):
    RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {
        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.suggested_place_row,
                parent,
                false
            )

            return ViewHolder(view)        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            return places.size
        }
    }
}