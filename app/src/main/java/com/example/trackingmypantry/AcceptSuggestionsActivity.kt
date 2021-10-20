package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.db.entities.ItemSuggestion
import com.example.trackingmypantry.db.entities.Place
import com.example.trackingmypantry.db.entities.PlaceSuggestion
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
                            val raw = Utils.byteArrayToItemSuggestion(payload)

                            if (raw.size == 4) {
                                itemSuggestions.add(
                                    ItemSuggestion(raw[0], raw[1], raw[2], null, raw[3])
                                )
                            } else {
                                itemSuggestions.add(
                                    ItemSuggestion(raw[0], raw[1], raw[2], raw[3], raw[4])
                                )
                            }
                        }

                        Utils.Companion.FollowingDataType.PLACE_TYPE -> {
                            val payload = Utils.payloadOf(value.data)
                            val raw = Utils.byteArrayToPlaceSuggestion(payload)

                            placeSuggestions.add(
                                PlaceSuggestion(raw.t1, raw.t2, raw.t3, raw.t4)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_accept_suggestions)


    }
}