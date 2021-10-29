package com.example.trackingmypantry

import android.bluetooth.BluetoothSocket
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.db.entities.ItemSuggestion
import com.example.trackingmypantry.db.entities.PlaceSuggestion
import com.example.trackingmypantry.lib.DbSingleton
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.adapters.IndexedArrayCallback
import com.example.trackingmypantry.lib.adapters.ReceivedSuggestionsAdapter
import com.example.trackingmypantry.lib.connectivity.bluetooth.BlueUtils
import com.example.trackingmypantry.lib.connectivity.bluetooth.ConnectThread
import com.example.trackingmypantry.lib.connectivity.bluetooth.MessageType
import com.example.trackingmypantry.lib.connectivity.bluetooth.ReceiveThread
import com.example.trackingmypantry.lib.data.Suggestion

class AcceptSuggestionsActivity : AppCompatActivity() {
    companion object {
        const val BLUETOOTH_SOCKET_EXTRA = "btSocket"
        const val BLUETOOTH_THREAD_EXTRA = "btAccept"
    }

    private val itemSuggestions = mutableListOf<ItemSuggestion>()
    private val placeSuggestions = mutableListOf<PlaceSuggestion>()
    private val suggestions = mutableListOf<Suggestion>()
    private val liveSuggestions = MutableLiveData<List<Suggestion>>(this.suggestions)

    private lateinit var receiveThread: ReceiveThread

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

                    /* Little hack to notify the observer that dataset changed (in case of
                    * collections, if the underlying collection has changes, the livedata will
                    * not update anyway). */
                    liveSuggestions.value = suggestions
                }
            }

            // Thread restart
            this@AcceptSuggestionsActivity.receiveThread.cancel()
            this@AcceptSuggestionsActivity.receiveThread.start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_accept_suggestions)

        val recyclerView: RecyclerView = this.findViewById(R.id.waitSuggestRecView)
        val endButton: AppCompatButton = this.findViewById(R.id.endConnectionButton)

        recyclerView.adapter = ReceivedSuggestionsAdapter(this.showInfo, arrayOf())
        recyclerView.layoutManager = LinearLayoutManager(this)

        endButton.setOnClickListener {
            this.finish()
        }

        this.liveSuggestions.observe(this, {
            recyclerView.adapter = ReceivedSuggestionsAdapter(this.showInfo, it.toTypedArray())
        })

        val socketKey = this.intent.extras!!.getInt(BLUETOOTH_SOCKET_EXTRA)
        val socket = Utils.getSavedValue(socketKey) as BluetoothSocket

        this.receiveThread = ReceiveThread(readHandler, socket)
        this.receiveThread.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        synchronized(this.itemSuggestions) {
            if (this.itemSuggestions.isNotEmpty()) {
                DbSingleton.getInstance(this)
                    .insertItemSuggestions(*this.itemSuggestions.toTypedArray())
            }
        }

        synchronized(this.placeSuggestions) {
            if (this.placeSuggestions.isNotEmpty()) {
                DbSingleton.getInstance(this)
                    .insertPlaceSuggestions(*this.placeSuggestions.toTypedArray())
            }
        }

        val threadKey = this.intent.extras!!.getInt(BLUETOOTH_THREAD_EXTRA)
        val thread = Utils.getSavedValue(threadKey) as ConnectThread
        thread.cancel()
    }

    private val showInfo: IndexedArrayCallback<Suggestion> = {
        val suggestion = it.array[it.index]

        if (suggestion.isItem()) {
            val itemSuggestion = suggestion.itemSuggestion!!

            val imageEncoding = itemSuggestion.image
            if (imageEncoding != null) {
                val imageView = ImageView(this)
                imageView.setImageBitmap(Utils.base64ToBitmap(imageEncoding))

                AlertDialog.Builder(this)
                    .setMessage(
                        "Barcode: " + itemSuggestion.barcode +
                        "\nDescription: " + itemSuggestion.description +
                        "\nUsername: " + itemSuggestion.user
                    )
                    .setView(imageView)
                    .setPositiveButton(R.string.positiveOk, null)
                    .show()
            } else {
                AlertDialog.Builder(this)
                    .setMessage(
                        "Barcode: " + itemSuggestion.barcode +
                        "\nDescription: " + itemSuggestion.description +
                        "\nUsername: " + itemSuggestion.user
                    )
                    .show()
            }
        } else if (suggestion.isPlace()) {
            val placeSuggestion = suggestion.placeSuggestion!!

            AlertDialog.Builder(this)
                .setMessage(
                    "Username: " + placeSuggestion.username
                )
                .setPositiveButton(R.string.positiveOk, null)
                .show()
        }
    }
}