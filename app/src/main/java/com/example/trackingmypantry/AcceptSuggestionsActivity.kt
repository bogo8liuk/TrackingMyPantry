package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.MutableLiveData
import com.example.trackingmypantry.db.entities.ItemSuggestion
import com.example.trackingmypantry.db.entities.PlaceSuggestion
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.connectivity.bluetooth.BlueUtils
import com.example.trackingmypantry.lib.connectivity.bluetooth.MessageType

class AcceptSuggestionsActivity : AppCompatActivity() {
    companion object {
        const val BLUETOOTH_SOCKET_EXTRA = "btSocket"
        const val BLUETOOTH_THREAD_EXTRA = "btAccept"
    }

    private val itemSuggestions = mutableListOf<ItemSuggestion>()
    private val placeSuggestions = mutableListOf<PlaceSuggestion>()

    private val readHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MessageType.READ_DATA -> {
                    val value = msg.obj as BlueUtils.IncomingData

                    when (Utils.encodedTypeOf(value.data)) {
                        Utils.Companion.FollowingDataType.ITEM_TYPE -> {
                            val payload = Utils.payloadOf(value.data)
                            val suggestion = Utils.byteArrayToItemSuggestion(payload)

                            itemSuggestions.add(suggestion)
                        }

                        Utils.Companion.FollowingDataType.PLACE_TYPE -> {
                            val payload = Utils.payloadOf(value.data)
                            val suggestion = Utils.byteArrayToPlaceSuggestion(payload)

                            placeSuggestions.add(suggestion)
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_accept_suggestions)

        val endButton: AppCompatButton = this.findViewById(R.id.endConnectionButton)

        endButton.setOnClickListener {
            DbSingleton.getInstance(this).insertItemSuggestions(*this.itemSuggestions.toTypedArray())
            DbSingleton.getInstance(this).insertPlaceSuggestions(*this.placeSuggestions.toTypedArray())

            this.finish()
        }

        val liveItemSuggestions = MutableLiveData<List<ItemSuggestion>>(this.itemSuggestions)
        val livePlaceSuggestions = MutableLiveData<List<PlaceSuggestion>>(this.placeSuggestions)

        liveItemSuggestions.observe(this, {
            //TODO
        })

        livePlaceSuggestions.observe(this, {
            //TODO
        })
    }
}