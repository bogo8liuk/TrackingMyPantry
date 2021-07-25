package com.example.trackingmypantry.lib

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.db.entities.Item
import java.util.*

class LocalItemsAdapter(private val items: Array<Item>):
    RecyclerView.Adapter<LocalItemsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameButton = view.findViewById<AppCompatButton>(R.id.localItemNameButton)
        val nameExpandedButton = view.findViewById<AppCompatButton>(R.id.localItemNameExpandedButton)
        val deleteButton = view.findViewById<AppCompatButton>(R.id.deleteButton)
        val descText = view.findViewById<TextView>(R.id.localDescText)
        val purchaseText = view.findViewById<TextView>(R.id.purchaseLocalItemText)
        val expirationText = view.findViewById<TextView>(R.id.expirationLocalItemText)

        init {
            /* The check of expiration date is done here because in `onBindViewHolder()`,
            * I cannot access the context. */
            val expiration = items[this.adapterPosition].expiration_date
            if (expiration != null && Date().before(expiration)) {  // Date() returns the allocation date
                this.nameButton.background = ContextCompat.getDrawable(view.context, R.color.red)
                this.nameExpandedButton.background = ContextCompat.getDrawable(view.context, R.color.red)
            }

            this.nameButton.setOnClickListener {
                this.nameButton.visibility = android.view.View.GONE
                this.nameExpandedButton.visibility = android.view.View.VISIBLE
                this.deleteButton.visibility = android.view.View.VISIBLE
                this.descText.visibility = android.view.View.VISIBLE
                this.purchaseText.visibility = android.view.View.VISIBLE
                this.expirationText.visibility = android.view.View.VISIBLE
            }

            this.nameExpandedButton.setOnClickListener {
                this.nameButton.visibility = android.view.View.VISIBLE
                this.nameExpandedButton.visibility = android.view.View.GONE
                this.deleteButton.visibility = android.view.View.GONE
                this.descText.visibility = android.view.View.GONE
                this.purchaseText.visibility = android.view.View.GONE
                this.expirationText.visibility = android.view.View.GONE
            }

            this.deleteButton.setOnClickListener {
                AlertDialog.Builder(view.context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this item from your grocery?")
                    .setNegativeButton(R.string.negative, null)
                    .setPositiveButton(R.string.positive, DialogInterface.OnClickListener { _, _ ->
                        // TODO: test if the buttons got deleted
                        DbSingleton.getInstance(view.context).deleteItems(items[this.adapterPosition])
                    })
                    .show()
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

    override fun onBindViewHolder(holder: LocalItemsAdapter.ViewHolder, position: Int) {
        holder.nameButton.text = items[position].name
        holder.nameExpandedButton.text = items[position].name
        holder.descText.text = items[position].description
        holder.purchaseText.text = "Purchase date: " + items[position].purchase_date // TODO: safe?
        holder.expirationText.text = "Expiration date: " + items[position].expiration_date
        // TODO: add a barcode text view in the linear layout
    }

    override fun getItemCount(): Int {
        return items.size
    }
}