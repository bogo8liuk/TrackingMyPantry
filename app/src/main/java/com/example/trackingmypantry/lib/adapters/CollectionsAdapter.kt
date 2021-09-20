package com.example.trackingmypantry.lib.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.example.trackingmypantry.LocalItemsActivity
import com.example.trackingmypantry.R
import com.example.trackingmypantry.db.entities.Collection

class CollectionsAdapter(private val context: Context, private val collections: Array<Collection>): BaseAdapter() {
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val MAX_COLOR = 6

    private fun getColor(position: Int): Int {
        when (position % MAX_COLOR) {
            0 -> return R.color.red
            1 -> return R.color.blue
            2 -> return R.color.green
            3 -> return R.color.orange
            4 -> return R.color.purple_500
            5 -> return R.color.lightblue
        }

        Log.e("Unreachable state", "There are just 6 possible colors for collections " +
                "UI and black is not among them")
        return R.color.black
    }

    private fun initView(view: View, position: Int): View {
        val collectionButton = view.findViewById<ImageButton>(R.id.collectionButton)
        val collectionText = view.findViewById<TextView>(R.id.collectionText)

        collectionButton.setBackgroundResource(this.getColor(position))
        collectionText.text = this.collections[position].name

        collectionButton.setOnClickListener {
            val currentActivity = it.context as Activity
            val intent = Intent(currentActivity, LocalItemsActivity::class.java)
            intent.putExtra("collection", collections[position].id)
            currentActivity.startActivity(intent)
        }

        return view
    }

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
        return convertView ?: this.initView(this.inflater.inflate(R.layout.collection_row, parent, false), position)
    }
}