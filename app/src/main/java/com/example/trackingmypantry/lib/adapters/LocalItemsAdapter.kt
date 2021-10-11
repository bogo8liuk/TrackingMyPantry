package com.example.trackingmypantry.lib.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.db.entities.Collection
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.Utils
import java.util.*

class LocalItemsAdapter(private val items: Array<Item>, private val collections: List<Collection>?):
    RecyclerView.Adapter<LocalItemsAdapter.ViewHolder>() {
    private var isExpanded = BooleanArray(items.size) { _ -> false }

    /**
     * firstBind is used to prevent the re-set of those already set fields
     * of the references of the viewholder that will keep their values
     * "forever" (i.e. the barcode text of the TextView `barcodeText`), when
     * one of the two nameButton is clicked.
     */
    private var firstBind = true

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameButton = view.findViewById<AppCompatButton>(R.id.localItemNameButton)
        val nameExpandedButton = view.findViewById<AppCompatButton>(R.id.localItemNameExpandedButton)
        val barcodeText = view.findViewById<TextView>(R.id.barcodeDescText)
        val descText = view.findViewById<TextView>(R.id.descLocalItemText)
        val quantityText = view.findViewById<TextView>(R.id.quantityText)
        val purchaseText = view.findViewById<TextView>(R.id.purchaseLocalItemText)
        val expirationText = view.findViewById<TextView>(R.id.expirationLocalItemText)
        val image = view.findViewById<ImageView>(R.id.itemImage)
        val handleItemLayout = view.findViewById<LinearLayout>(R.id.handleItemLayout)
        val addQuantityButton = handleItemLayout.findViewById<AppCompatImageButton>(R.id.addQuantityButton)
        val removeQuantityButton = handleItemLayout.findViewById<AppCompatImageButton>(R.id.removeQuantityButton)
        val changeCollectionButton = handleItemLayout.findViewById<AppCompatButton>(R.id.changeCollectionButton)

        /**
         * It returns the collection id, given a position of a collection. It is sured not
         * to have conflicts between names because they are unique.
         */
        private fun getCollectionFromName(position: Int, collections: List<Collection>): Long? {
            for ((c, collection) in collections.withIndex()) {
                if (c == position) {
                    return collection.id
                }
            }

            Log.e("Unreachable", "Value out of range while iterating list of collections")
            return null
        }

        init {
            val context = view.context

            this.addQuantityButton.setOnClickListener {
                val quantityPicker = NumberPicker(context)
                quantityPicker.minValue = 1
                quantityPicker.maxValue = 50    // arbitrary
                AlertDialog.Builder(context)
                    .setTitle("Buy")
                    .setMessage("Choose the quantity of this product you want to buy")
                    .setView(quantityPicker)
                    .setNegativeButton(R.string.negativeCanc, null)
                    .setPositiveButton(R.string.buy) { _, _ ->
                        DbSingleton.getInstance(context).changeItemQuantity(
                            items[this.adapterPosition].id,
                            quantityPicker.value
                        )
                    }
                    .show()
            }

            this.removeQuantityButton.setOnClickListener {
                val quantityPicker = NumberPicker(context)
                quantityPicker.minValue = 1
                quantityPicker.maxValue = items[this.adapterPosition].quantity
                AlertDialog.Builder(context)
                    .setTitle("Remove")
                    .setMessage("Choose the quantity of this product you want to remove")
                    .setView(quantityPicker)
                    .setNegativeButton(R.string.negativeCanc, null)
                    .setPositiveButton(R.string.remove) { _, _ ->
                        DbSingleton.getInstance(context).changeItemQuantity(
                            items[this.adapterPosition].id,
                            -quantityPicker.value
                        )
                    }
                    .show()
            }

            if (collections == null) {
                this.changeCollectionButton.setOnClickListener {
                    DbSingleton.getInstance(context).removeItemFromCollection(items[this.adapterPosition].id)
                }
            } else {

                this.changeCollectionButton.setOnClickListener {
                    if (collections.isEmpty()) {
                        Utils.toastShow(context, "Create a collection before")
                    } else {
                        val values = mutableListOf<String>()
                        for (collection in collections) {
                            values.add(collection.name)
                        }

                        val collectionPicker = NumberPicker(context)
                        collectionPicker.minValue = 0
                        collectionPicker.maxValue = values.size - 1
                        collectionPicker.displayedValues = values.toTypedArray()

                        AlertDialog.Builder(context)
                            .setTitle("Collections")
                            .setMessage("Choose a collection")
                            .setView(collectionPicker)
                            .setNegativeButton(R.string.negativeCanc, null)    // do nothing
                            .setPositiveButton(R.string.choose) { _, _ ->
                                DbSingleton.getInstance(context).insertItemIntoCollection(
                                    items[this.adapterPosition].id,
                                    this.getCollectionFromName(collectionPicker.value, collections)!!
                                )
                            }
                            .show()
                    }
                }
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
        if (firstBind) {
            val context = holder.barcodeText.context    // getting the context from a view (whatever it is)

            val expiration = items[position].expiration_date
            if (expiration != null && Date().after(expiration)) {  // Date() returns the allocation date
                holder.nameButton.background = ContextCompat.getDrawable(context, R.color.red)
                holder.nameExpandedButton.background = ContextCompat.getDrawable(context, R.color.red)
            }
            holder.nameButton.text = items[position].name
            holder.nameExpandedButton.text = items[position].name
            holder.descText.text = items[position].description
            holder.quantityText.text = "Quantity: " + items[position].quantity.toString()
            holder.purchaseText.text = "Purchase date: " + items[position].purchase_date // TODO: safe?
            holder.expirationText.text = "Expiration date: " + items[position].expiration_date
            holder.barcodeText.text = "Barcode: " + items[position].barcode

            if (this.collections == null) {
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

            firstBind = false
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