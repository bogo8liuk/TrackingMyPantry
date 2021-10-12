package com.example.trackingmypantry.lib.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.db.entities.ItemSuggestion
import com.example.trackingmypantry.db.entities.PlaceSuggestion
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.Utils

class Suggested {
    inner class ItemsAdapter(private val items: Array<ItemSuggestion>):
    RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
        private var isExpanded = BooleanArray(items.size) { _ -> false }
        /**
         * firstBindAt is used to prevent the re-set of those already set fields
         * of the references of the viewholder that will keep their values
         * "forever" (i.e. the barcode text of the TextView `barcodeText`), when
         * one of the two nameButton is clicked.
         */
        private var firstBindAt = BooleanArray(items.size) { _ -> true }

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val nameButton: AppCompatButton = view.findViewById(R.id.suggestedItemNameButton)
            val nameExpandedButton: AppCompatButton = view.findViewById(R.id.suggestedItemNameExpandedButton)
            val barcodeText:TextView = view.findViewById(R.id.suggestedItemBarcode)
            val descriptionText: TextView = view.findViewById(R.id.suggestedItemDescription)
            val userText: TextView = view.findViewById(R.id.suggestedItemUser)
            val image: ImageView = view.findViewById(R.id.suggestedItemImage)
            val deleteButton: AppCompatButton = view.findViewById(R.id.removeSuggestedItemButton)

            init {
                this.deleteButton.setOnClickListener {
                    DbSingleton.getInstance(view.context) //TODO: delete suggestion item
                }
            }
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
            if (firstBindAt[position]) {
                holder.nameButton.text = items[position].name
                holder.nameExpandedButton.text = items[position].name
                holder.barcodeText.text = items[position].barcode
                holder.descriptionText.text = items[position].description
                holder.userText.text = items[position].user

                val bitmap = items[position].image
                if (bitmap != null) {
                    holder.image.setImageBitmap(
                        Utils.base64ToBitmap(bitmap)
                    )
                }

                firstBindAt[position] = false
            }

            if (this.isExpanded[position]) {
                holder.nameButton.visibility = android.view.View.GONE
                holder.nameExpandedButton.visibility = android.view.View.VISIBLE
                holder.barcodeText.visibility = android.view.View.VISIBLE
                holder.descriptionText.visibility = android.view.View.VISIBLE
                holder.userText.visibility = android.view.View.VISIBLE
                holder.image.visibility = android.view.View.VISIBLE
            } else {
                holder.nameButton.visibility = android.view.View.VISIBLE
                holder.nameExpandedButton.visibility = android.view.View.GONE
                holder.barcodeText.visibility = android.view.View.GONE
                holder.descriptionText.visibility = android.view.View.GONE
                holder.userText.visibility = android.view.View.GONE
                holder.image.visibility = android.view.View.GONE
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
            return items.size
        }
    }

    inner class PlacesAdapter(private val places: Array<PlaceSuggestion>):
    RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {
        private var isExpanded = BooleanArray(places.size) { _ -> false }
        // same as above
        private var firstBindAt = BooleanArray(places.size) { _ -> true }

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val nameButton: AppCompatButton = view.findViewById(R.id.suggestedPlaceNameButton)
            val nameExpandedButton: AppCompatButton = view.findViewById(R.id.suggestedPlaceNameExpandedButton)
            val layout: LinearLayout = view.findViewById(R.id.handleSuggestionLayout)
            val deleteButton: AppCompatButton = layout.findViewById(R.id.removeSuggestedPlaceButton)
            val sendButton: AppCompatButton = layout.findViewById(R.id.sendSuggestedPlaceButton)

            init {
                //TODO: delete and send place suggestion
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
            if (firstBindAt[position]) {
                holder.nameButton.text = places[position].title
                holder.nameExpandedButton.text = places[position].title

                firstBindAt[position] = false
            }

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
}