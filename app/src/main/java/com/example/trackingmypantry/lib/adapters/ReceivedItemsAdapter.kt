package com.example.trackingmypantry.lib.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.lib.*
import com.example.trackingmypantry.lib.data.Product

class ReceivedItemsAdapter(
    private val chooseCallback: IndexedArrayCallback<Product>,
    private val expDateCallback: IndexedArrayCallback<Product>,
    private val products: Array<Product>
): RecyclerView.Adapter<ReceivedItemsAdapter.ViewHolder>() {
    private var isExpanded = BooleanArray(products.size) { _ -> false }
    /**
     * firstBindAt is used to prevent the re-set of those already set fields
     * of the references of the viewholder that will keep their values
     * "forever" (i.e. the barcode text of the TextView `barcodeText`), when
     * one of the two nameButton is clicked.
     */
    private var firstBindAt = BooleanArray(products.size) { _ -> true }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameButton = view.findViewById<AppCompatButton>(R.id.receivedItemNameButton)
        val nameExpandedButton = view.findViewById<AppCompatButton>(R.id.receivedItemNameExpandedButton)
        val descriptionTextView = view.findViewById<TextView>(R.id.receivedItemDescription)
        val chooseButton = view.findViewById<AppCompatButton>(R.id.receivedItemChooseButton)
        val setExpButton = view.findViewById<AppCompatButton>(R.id.receivedItemSetExpButton)
        val image = view.findViewById<ImageView>(R.id.receivedItemImage)

        init {
            this.chooseButton.setOnClickListener {
                chooseCallback(IndexedArray(products, this.adapterPosition))
            }

            this.setExpButton.setOnClickListener {
                expDateCallback(IndexedArray(products, this.adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.received_item_row,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (firstBindAt[position]) {
            holder.nameButton.text = products[position].name
            holder.nameExpandedButton.text = products[position].name
            holder.descriptionTextView.text = products[position].description
            val bitmap = products[position].image?.let { Utils.base64ToBitmap(it) }
            if (bitmap != null) {
                holder.image.setImageBitmap(bitmap)
            }

            firstBindAt[position] = false
        }

        if (this.isExpanded[position]) {
            holder.nameButton.visibility = android.view.View.GONE
            holder.nameExpandedButton.visibility = android.view.View.VISIBLE
            holder.descriptionTextView.visibility = android.view.View.VISIBLE
            holder.chooseButton.visibility = android.view.View.VISIBLE
            holder.setExpButton.visibility = android.view.View.VISIBLE
            holder.image.visibility = android.view.View.VISIBLE
        } else {
            holder.nameExpandedButton.visibility = android.view.View.GONE
            holder.nameButton.visibility = android.view.View.VISIBLE
            holder.descriptionTextView.visibility = android.view.View.GONE
            holder.chooseButton.visibility = android.view.View.GONE
            holder.setExpButton.visibility = android.view.View.GONE
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
        return products.size
    }
}