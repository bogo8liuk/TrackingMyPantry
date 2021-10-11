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
import com.example.trackingmypantry.lib.Utils

class Suggested {
    inner class ItemsAdapter(private val items: Array<ItemSuggestion>):
    RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
        private var isExpanded = BooleanArray(items.size) { _ -> false }

        /**
         * firstBind is used to prevent the re-set of those already set fields
         * of the references of the viewholder that will keep their values
         * "forever" (i.e. the barcode text of the TextView `barcodeText`), when
         * one of the two nameButton is clicked.
         */
        private var firstBind = true

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val nameButton: AppCompatButton = view.findViewById(R.id.suggestedItemNameButton)
            val nameExpandedButton: AppCompatButton = view.findViewById(R.id.suggestedItemNameExpandedButton)
            val barcodeText:TextView = view.findViewById(R.id.suggestedItemBarcode)
            val descriptionText: TextView = view.findViewById(R.id.suggestedItemDescription)
            val userText: TextView = view.findViewById(R.id.suggestedItemUser)
            val image: ImageView = view.findViewById(R.id.suggestedItemImage)
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
            if (firstBind) {
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

                firstBind = false
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