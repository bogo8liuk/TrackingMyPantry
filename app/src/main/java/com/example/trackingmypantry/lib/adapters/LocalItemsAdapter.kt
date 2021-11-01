package com.example.trackingmypantry.lib.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.Utils
import java.util.*

class LocalItemsAdapter(
    private val addQuantityCallback: IndexedArrayCallback<Item>,
    private val removeQuantityCallback: IndexedArrayCallback<Item>,
    private val changeCollectionCallback: IndexedArrayCallback<Item>,
    private val isToRemove: Boolean,
    private val items: Array<Item>
): RecyclerView.Adapter<LocalItemsAdapter.ViewHolder>() {
    private var isExpanded = BooleanArray(items.size) { _ -> false }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameButton: AppCompatButton = view.findViewById(R.id.localItemNameButton)
        val nameExpandedButton: AppCompatButton = view.findViewById(R.id.localItemNameExpandedButton)
        val barcodeText: TextView = view.findViewById(R.id.barcodeDescText)
        val descText: TextView = view.findViewById(R.id.descLocalItemText)
        val quantityText: TextView = view.findViewById(R.id.quantityText)
        val purchaseText: TextView = view.findViewById(R.id.purchaseLocalItemText)
        val expirationText: TextView = view.findViewById(R.id.expirationLocalItemText)
        val image: ImageView = view.findViewById(R.id.itemImage)
        val handleItemLayout: LinearLayout = view.findViewById(R.id.handleItemLayout)
        val addQuantityButton: AppCompatImageButton = handleItemLayout.findViewById(R.id.addQuantityButton)
        val removeQuantityButton: AppCompatImageButton = handleItemLayout.findViewById(R.id.removeQuantityButton)
        val changeCollectionButton: AppCompatButton = handleItemLayout.findViewById(R.id.changeCollectionButton)

        init {
            this.addQuantityButton.setOnClickListener {
                addQuantityCallback(IndexedArray(items, this.adapterPosition))
            }

            this.removeQuantityButton.setOnClickListener {
                removeQuantityCallback(IndexedArray(items, this.adapterPosition))
            }

            this.changeCollectionButton.setOnClickListener {
                changeCollectionCallback(IndexedArray(items, this.adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.local_item_row,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expiration = items[position].expiration_date
        if (expiration != null && Date().after(expiration)) {  // Date() returns the allocation date
            holder.nameButton.setBackgroundColor(Color.RED)
            holder.nameExpandedButton.setBackgroundColor(Color.RED)
        } else {
            holder.nameButton.setBackgroundColor(Color.WHITE)
            holder.nameExpandedButton.setBackgroundColor(Color.WHITE)
        }

        holder.nameButton.text = items[position].name
        holder.nameExpandedButton.text = items[position].name
        holder.descText.text = items[position].description
        holder.quantityText.text = "Quantity: " + items[position].quantity.toString()
        holder.purchaseText.text = "Purchase date: " + items[position].purchase_date // TODO: safe?
        holder.expirationText.text = "Expiration date: " + (items[position].expiration_date ?: "Unspecified")
        holder.barcodeText.text = "Barcode: " + items[position].barcode

        if (this.isToRemove) {
            holder.changeCollectionButton.text = "Remove from collection"
        } else {
            holder.changeCollectionButton.text = "Add to collection"
        }

        if (items[position].image != null) {
            val bitmap = Utils.base64ToBitmap(items[position].image!!)

            if (bitmap != null) {
                holder.image.setImageBitmap(bitmap)
            }
        }

        if (this.isExpanded[position]) {
            holder.nameButton.visibility = android.view.View.GONE
            holder.nameExpandedButton.visibility = android.view.View.VISIBLE
            holder.barcodeText.visibility = android.view.View.VISIBLE
            holder.descText.visibility = android.view.View.VISIBLE
            holder.quantityText.visibility = android.view.View.VISIBLE
            holder.purchaseText.visibility = android.view.View.VISIBLE
            holder.expirationText.visibility = android.view.View.VISIBLE
            holder.handleItemLayout.visibility = android.view.View.VISIBLE
            holder.image.visibility = android.view.View.VISIBLE
        } else {
            holder.nameButton.visibility = android.view.View.VISIBLE
            holder.nameExpandedButton.visibility = android.view.View.GONE
            holder.barcodeText.visibility = android.view.View.GONE
            holder.descText.visibility = android.view.View.GONE
            holder.quantityText.visibility = android.view.View.GONE
            holder.purchaseText.visibility = android.view.View.GONE
            holder.expirationText.visibility = android.view.View.GONE
            holder.handleItemLayout.visibility = android.view.View.GONE
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