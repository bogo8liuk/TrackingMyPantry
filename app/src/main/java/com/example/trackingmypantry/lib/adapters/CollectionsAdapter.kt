package com.example.trackingmypantry.lib.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.trackingmypantry.db.entities.Collection

class CollectionsAdapter(private val collections: Array<Collection>): BaseAdapter() {
    override fun getCount(): Int {
        return this.collections.size
    }

    override fun getItem(position: Int): Any {
        return this.collections[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }
}