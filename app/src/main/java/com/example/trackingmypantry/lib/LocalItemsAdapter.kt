package com.example.trackingmypantry.lib

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
                this.nameExpandedButton.visibility = android.view.View.GONE
            }

            this.nameExpandedButton.setOnClickListener {
                this.nameButton.visibility = android.view.View.GONE
                this.nameExpandedButton.visibility = android.view.View.GONE
            }

            this.deleteButton.setOnClickListener {
                // TODO
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalItemsAdapter.ViewHolder {

    }

    override fun onBindViewHolder(holder: LocalItemsAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {

    }
}