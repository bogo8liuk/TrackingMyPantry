package com.example.trackingmypantry.lib.adapters

import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton
import java.util.*

class LocalItemsAdapter(private val items: Array<Item>, private val withCollections: Boolean):
    RecyclerView.Adapter<LocalItemsAdapter.ViewHolder>() {
    private var isExpanded = BooleanArray(items.size) { _ -> false }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameButton = view.findViewById<AppCompatButton>(R.id.localItemNameButton)
        val nameExpandedButton = view.findViewById<AppCompatButton>(R.id.localItemNameExpandedButton)
        val deleteButton = view.findViewById<AppCompatButton>(R.id.deleteButton)
        val barcodeText = view.findViewById<TextView>(R.id.barcodeDescText)
        val descText = view.findViewById<TextView>(R.id.descLocalItemText)
        val purchaseText = view.findViewById<TextView>(R.id.purchaseLocalItemText)
        val expirationText = view.findViewById<TextView>(R.id.expirationLocalItemText)
        val handleItemLayout = view.findViewById<LinearLayout>(R.id.handleItemLayout)
        val addQuantityButton = handleItemLayout.findViewById<AppCompatImageButton>(R.id.addQuantityButton)
        val removeQuantityButton = handleItemLayout.findViewById<AppCompatImageButton>(R.id.removeQuantityButton)
        val changeCollectionButton = handleItemLayout.findViewById<AppCompatButton>(R.id.changeCollectionButton)

        init {
            this.deleteButton.setOnClickListener {
                AlertDialog.Builder(view.context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this item from your grocery?")
                    .setNegativeButton(R.string.negative, null)
                    .setPositiveButton(R.string.positive, DialogInterface.OnClickListener { _, _ ->
                        // TODO: delete delete button
                    })
                    .show()
            }

            if (withCollections) {
                this.changeCollectionButton.setOnClickListener {
                    //TODO: remove collection
                }
            } else {
                this.changeCollectionButton.setOnClickListener {
                    //TODO: add collection
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.local_item_row,
            parent,
            false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /* The check of expiration date is done here because in `onBindViewHolder()`,
            * I cannot access the context. */
        val expiration = items[position].expiration_date
        if (expiration != null && Date().after(expiration)) {  // Date() returns the allocation date
            holder.nameButton.background = ContextCompat.getDrawable(holder.barcodeText.context, R.color.red)
            holder.nameExpandedButton.background = ContextCompat.getDrawable(holder.barcodeText.context, R.color.red)
        }
        holder.nameButton.text = items[position].name
        holder.nameExpandedButton.text = items[position].name
        holder.descText.text = items[position].description
        holder.purchaseText.text = "Purchase date: " + items[position].purchase_date // TODO: safe?
        holder.expirationText.text = "Expiration date: " + items[position].expiration_date
        holder.barcodeText.text = "Barcode: " + items[position].barcode
        if (this.withCollections) {
            holder.changeCollectionButton.text = "Remove from collection"
        } else {
            holder.changeCollectionButton.text = "Add to collection"
        }

        if (this.isExpanded[position]) {
            holder.nameButton.visibility = android.view.View.GONE
            holder.nameExpandedButton.visibility = android.view.View.VISIBLE
            holder.deleteButton.visibility = android.view.View.VISIBLE
            holder.barcodeText.visibility = android.view.View.VISIBLE
            holder.descText.visibility = android.view.View.VISIBLE
            holder.purchaseText.visibility = android.view.View.VISIBLE
            holder.expirationText.visibility = android.view.View.VISIBLE
            holder.handleItemLayout.visibility = android.view.View.VISIBLE
        } else {
            holder.nameButton.visibility = android.view.View.VISIBLE
            holder.nameExpandedButton.visibility = android.view.View.GONE
            holder.deleteButton.visibility = android.view.View.GONE
            holder.barcodeText.visibility = android.view.View.GONE
            holder.descText.visibility = android.view.View.GONE
            holder.purchaseText.visibility = android.view.View.GONE
            holder.expirationText.visibility = android.view.View.GONE
            holder.handleItemLayout.visibility = android.view.View.GONE
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