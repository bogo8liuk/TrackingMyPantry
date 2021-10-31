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
import com.example.trackingmypantry.lib.Utils

class SuggestedItemsAdapter(
    private val deleteCallback: IndexedArrayCallback<ItemSuggestion>,
    private val suggestions: Array<ItemSuggestion>
): RecyclerView.Adapter<SuggestedItemsAdapter.ViewHolder>() {
    private var isExpanded = BooleanArray(suggestions.size) { _ -> false }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameButton: AppCompatButton = view.findViewById(R.id.suggestedItemNameButton)
        val nameExpandedButton: AppCompatButton =
            view.findViewById(R.id.suggestedItemNameExpandedButton)
        val barcodeText: TextView = view.findViewById(R.id.suggestedItemBarcode)
        val descriptionText: TextView = view.findViewById(R.id.suggestedItemDescription)
        val userText: TextView = view.findViewById(R.id.suggestedItemUser)
        val image: ImageView = view.findViewById(R.id.suggestedItemImage)
        val deleteButton: AppCompatButton = view.findViewById(R.id.removeSuggestedItemButton)

        init {
            this.deleteButton.setOnClickListener {
                deleteCallback(IndexedArray(suggestions, this.adapterPosition))
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
        holder.nameButton.text = suggestions[position].name
        holder.nameExpandedButton.text = suggestions[position].name
        holder.barcodeText.text = suggestions[position].barcode
        holder.descriptionText.text = suggestions[position].description
        holder.userText.text = suggestions[position].user

        val bitmap = suggestions[position].image
        if (bitmap != null) {
            holder.image.setImageBitmap(
                Utils.base64ToBitmap(bitmap)
            )
        }

        if (this.isExpanded[position]) {
            holder.nameButton.visibility = android.view.View.GONE
            holder.nameExpandedButton.visibility = android.view.View.VISIBLE
            holder.barcodeText.visibility = android.view.View.VISIBLE
            holder.descriptionText.visibility = android.view.View.VISIBLE
            holder.userText.visibility = android.view.View.VISIBLE
            holder.image.visibility = android.view.View.VISIBLE
            holder.deleteButton.visibility = android.view.View.VISIBLE
        } else {
            holder.nameButton.visibility = android.view.View.VISIBLE
            holder.nameExpandedButton.visibility = android.view.View.GONE
            holder.barcodeText.visibility = android.view.View.GONE
            holder.descriptionText.visibility = android.view.View.GONE
            holder.userText.visibility = android.view.View.GONE
            holder.image.visibility = android.view.View.GONE
            holder.deleteButton.visibility = android.view.View.GONE
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
        return suggestions.size
    }
}