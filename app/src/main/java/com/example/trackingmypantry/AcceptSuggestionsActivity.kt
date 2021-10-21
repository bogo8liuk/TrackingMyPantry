package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.db.entities.ItemSuggestion
import com.example.trackingmypantry.db.entities.PlaceSuggestion
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.connectivity.bluetooth.BlueUtils
import com.example.trackingmypantry.lib.connectivity.bluetooth.ConnectThread
import com.example.trackingmypantry.lib.connectivity.bluetooth.MessageType
import com.example.trackingmypantry.lib.data.Suggestion

class AcceptSuggestionsActivity : AppCompatActivity() {
    companion object {
        const val BLUETOOTH_SOCKET_EXTRA = "btSocket"
        const val BLUETOOTH_THREAD_EXTRA = "btAccept"
    }

    private val itemSuggestions = mutableListOf<ItemSuggestion>()
    private val placeSuggestions = mutableListOf<PlaceSuggestion>()
    private val suggestions = mutableListOf<Suggestion>()

    private val readHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MessageType.READ_DATA -> {
                    val value = msg.obj as BlueUtils.IncomingData

                    when (Utils.encodedTypeOf(value.data)) {
                        Utils.Companion.FollowingDataType.ITEM_TYPE -> {
                            val payload = Utils.payloadOf(value.data)
                            val itemSuggestion = Utils.byteArrayToItemSuggestion(payload)
                            val suggestion = Suggestion()
                            suggestion.setItemSuggestion(itemSuggestion)

                            synchronized(suggestions) {
                                suggestions.add(suggestion)
                            }

                            synchronized(itemSuggestions) {
                                itemSuggestions.add(itemSuggestion)
                            }
                        }

                        Utils.Companion.FollowingDataType.PLACE_TYPE -> {
                            val payload = Utils.payloadOf(value.data)
                            val placeSuggestion = Utils.byteArrayToPlaceSuggestion(payload)
                            val suggestion = Suggestion()
                            suggestion.setPlaceSuggestion(placeSuggestion)

                            synchronized(suggestions) {
                                suggestions.add(suggestion)
                            }

                            synchronized(placeSuggestions) {
                                placeSuggestions.add(placeSuggestion)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_accept_suggestions)

        val recyclerView: RecyclerView = this.findViewById(R.id.waitSuggestRecView)
        val endButton: AppCompatButton = this.findViewById(R.id.endConnectionButton)

        //TODO: recyclerView.adapter =
        recyclerView.layoutManager = LinearLayoutManager(this)

        endButton.setOnClickListener {
            this.finish()
        }

        val liveSuggestions = MutableLiveData<List<Suggestion>>(this.suggestions)
        liveSuggestions.observe(this, {
            //TODO: recyclerView.adapter =
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        synchronized(this.itemSuggestions) {
            synchronized(this.placeSuggestions) {
                DbSingleton.getInstance(this)
                    .insertItemSuggestions(*this.itemSuggestions.toTypedArray())
                DbSingleton.getInstance(this)
                    .insertPlaceSuggestions(*this.placeSuggestions.toTypedArray())
            }
        }

        val threadKey = this.intent.extras!!.getInt(BLUETOOTH_THREAD_EXTRA)
        val thread = Utils.getSavedValue(threadKey) as ConnectThread
        thread.cancel()
    }
}