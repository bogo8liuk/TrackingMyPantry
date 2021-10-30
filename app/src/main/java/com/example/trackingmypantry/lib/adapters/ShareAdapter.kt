package com.example.trackingmypantry.lib.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.db.entities.Place
import kotlin.reflect.KClass

class ShareAdapter<T: Any>(
    private val clazz: KClass<T>,
    private val sendCallback: IndexedArrayCallback<T>,
    private val elements: Array<T>
): RecyclerView.Adapter<ShareAdapter<T>.ViewHolder>() {

    init {
        if (clazz != Item::class && clazz != Place::class) {
            throw RuntimeException()
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val sendButton: AppCompatButton = view.findViewById(R.id.elementToShareButton)

        init {
            this.sendButton.setOnClickListener {
                sendCallback(IndexedArray(elements, this.adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.element_to_share_row,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (clazz == Item::class) {
            holder.sendButton.text = (elements[position] as Item).name
        } else if (clazz == Place::class) {
            holder.sendButton.text = (elements[position] as Place).title
        }
    }

    override fun getItemCount(): Int {
        return elements.size
    }
}