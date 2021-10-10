package com.example.trackingmypantry.lib.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.db.entities.ItemSuggestion
import com.example.trackingmypantry.db.entities.PlaceSuggestion

class Suggested {
    inner class ItemsAdapter(items: Array<ItemSuggestion>):
    RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

    }

    inner class PlacesAdapter(places: Array<PlaceSuggestion>):
    RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {
        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }
    }
}