package com.example.trackingmypantry.lib.adapters

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.*
import com.example.trackingmypantry.lib.credentials.TokenHandler
import com.example.trackingmypantry.lib.credentials.TokenType
import com.example.trackingmypantry.lib.data.Product
import com.example.trackingmypantry.lib.net.HttpHandler
import java.util.*

class ReceivedItemsAdapter(private val products: Array<Product>):
    RecyclerView.Adapter<ReceivedItemsAdapter.ViewHolder>() {
    private var isExpanded = BooleanArray(products.size) { _ -> false }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameButton = view.findViewById<AppCompatButton>(R.id.receivedItemNameButton)
        val nameExpandedButton = view.findViewById<AppCompatButton>(R.id.receivedItemNameExpandedButton)
        val descriptionTextView = view.findViewById<TextView>(R.id.receivedItemDescription)
        val chooseButton = view.findViewById<AppCompatButton>(R.id.receivedItemChooseButton)
        val image = view.findViewById<ImageView>(R.id.receivedItemImage)

        private val MIN_RATE = 1
        private val MAX_RATE = 5

        init {
            this.chooseButton.setOnClickListener {
                var ratePicker = NumberPicker(view.context)
                ratePicker.minValue = MIN_RATE
                ratePicker.maxValue = MAX_RATE
                var datePicker = DatePicker(view.context)
                datePicker.minDate = Date().time
                AlertDialog.Builder(view.context)
                    .setTitle("Rate")
                    .setMessage("Select a rating for the product you chose")
                    .setView(ratePicker)
                    .setView(datePicker)
                    .setNegativeButton(R.string.negative1, null)
                    .setPositiveButton(R.string.send, DialogInterface.OnClickListener { _, _ ->
                        HttpHandler.serviceVoteProduct(
                            view.context,
                            TokenHandler.getToken(view.context, TokenType.ACCESS),
                            TokenHandler.getToken(view.context, TokenType.SESSION),
                            ratePicker.value,
                            products[this.adapterPosition].id,
                            { res ->
                                DbSingleton.getInstance(view.context).insertItems(
                                    Item(
                                        0,
                                        products[this.adapterPosition].barcode,
                                        products[this.adapterPosition].name,
                                        products[this.adapterPosition].description,
                                        null, //TODO: image
                                        Date(),
                                        Calendar.getInstance().also {
                                            it.set(datePicker.year, datePicker.month, datePicker.dayOfMonth) }
                                            .time,
                                        null
                                    )
                                )
                                val currentActivity = view.context as Activity
                                currentActivity.setResult(RESULT_OK, Intent())
                                currentActivity.finish()
                            },
                            { statusCode, err ->
                                val currentActivity = view.context as Activity
                                if (statusCode == 401) {
                                    currentActivity.setResult(ResultCode.EXPIRED_TOKEN, Intent())
                                    currentActivity.finish()
                                } else if (statusCode == 403) {
                                    currentActivity.setResult(ResultCode.INVALID_SESSION_TOKEN, Intent())
                                    currentActivity.finish()
                                } else {
                                    currentActivity.setResult(ResultCode.NETWORK_ERR, Intent())
                                    currentActivity.finish()
                                }
                            }
                        )
                    })
                    .show()
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
        holder.nameButton.text = products[position].name
        holder.nameExpandedButton.text = products[position].name
        holder.descriptionTextView.text = products[position].description
        val bitmap = Utils.base64ToBitmap(products[position].image)
        if (bitmap != null) {
            holder.image.setImageBitmap(bitmap)
        }
        if (this.isExpanded[position]) {
            holder.nameButton.visibility = android.view.View.GONE
            holder.nameExpandedButton.visibility = android.view.View.VISIBLE
            holder.descriptionTextView.visibility = android.view.View.VISIBLE
            holder.chooseButton.visibility = android.view.View.VISIBLE
            holder.image.visibility = android.view.View.VISIBLE
        } else {
            holder.nameExpandedButton.visibility = android.view.View.GONE
            holder.nameButton.visibility = android.view.View.VISIBLE
            holder.descriptionTextView.visibility = android.view.View.GONE
            holder.chooseButton.visibility = android.view.View.GONE
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