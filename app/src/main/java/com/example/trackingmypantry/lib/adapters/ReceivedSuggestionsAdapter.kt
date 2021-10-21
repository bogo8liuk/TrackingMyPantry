package com.example.trackingmypantry.lib.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.lib.data.Suggestion

class ReceivedSuggestionsAdapter(
    private val suggestionCallback: IndexedArrayCallback<Suggestion>,
    private val suggestions: Array<Suggestion>
): RecyclerView.Adapter<ReceivedSuggestionsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val suggestionButton = view.findViewById<AppCompatButton>(R.id.receivedSuggestionButton)

        init {
            suggestionButton.setOnClickListener {
                suggestionCallback(IndexedArray(suggestions, this.adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.received_suggestion_row,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (suggestions[position].isItem()) {
            holder.suggestionButton.text = suggestions[position].itemSuggestion!!.name

            holder.suggestionButton.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_location_on_dark_24, 0, 0, 0
            )
        } else if (suggestions[position].isPlace()) {
            holder.suggestionButton.text = suggestions[position].placeSuggestion!!.title

            holder.suggestionButton.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_food_bank_24, 0, 0, 0
            )
        }
    }

    override fun getItemCount(): Int {
        return this.suggestions.size
    }
}