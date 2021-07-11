package com.example.trackingmypantry.lib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.lib.data.Product

class ReceivedItemsAdapter(private val products: Array<Product>):
    RecyclerView.Adapter<ReceivedItemsAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameButton = view.findViewById<AppCompatButton>(R.id.receivedItemNameButton)
        val nameExpandedButton = view.findViewById<AppCompatButton>(R.id.receivedItemNameExpandedButton)
        val descriptionTextView = view.findViewById<TextView>(R.id.receivedItemDescription)
        val chooseButton = view.findViewById<AppCompatButton>(R.id.receivedItemChooseButton)

        init {
            nameButton.setOnClickListener {
                it.visibility = android.view.View.GONE
                nameExpandedButton.visibility = android.view.View.VISIBLE
                descriptionTextView.visibility = android.view.View.VISIBLE
                chooseButton.visibility = android.view.View.VISIBLE
            }

            nameExpandedButton.setOnClickListener {
                it.visibility = android.view.View.GONE
                nameButton.visibility = android.view.View.VISIBLE
                descriptionTextView.visibility = android.view.View.GONE
                chooseButton.visibility = android.view.View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.received_item_row,
            parent,
            false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameButton.text = products[position].name
        holder.nameExpandedButton.text = products[position].name
        holder.descriptionTextView.text = products[position].description
    }

    override fun getItemCount(): Int {
        return products.size
    }
}