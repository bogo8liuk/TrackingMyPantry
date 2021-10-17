package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.db.entities.Place
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.adapters.IndexedArrayCallback
import com.example.trackingmypantry.lib.adapters.ShareAdapter
import com.example.trackingmypantry.lib.connectivity.bluetooth.DataTransfer
import com.example.trackingmypantry.lib.connectivity.bluetooth.MessageType
import com.example.trackingmypantry.lib.viewmodels.DefaultAppViewModelFactory
import com.example.trackingmypantry.lib.viewmodels.LocalItemsViewModel
import com.example.trackingmypantry.lib.viewmodels.LocalItemsViewModelFactory
import com.example.trackingmypantry.lib.viewmodels.LocationsViewModel

class ShareActivity : AppCompatActivity() {
    companion object {
        const val BLUETOOTH_SOCKET_EXTRA = "btSocket"
        const val BLUETOOTH_SOCKET_KEY_EXTRA = 0
    }

    private val writeHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MessageType.WRITE_DATA -> {
                    val thread = msg.obj as DataTransfer.SendThread
                    thread.cancel()
                }

                MessageType.ERROR_WRITE -> {
                    val thread = msg.obj as DataTransfer.SendThread
                    thread.cancel()

                    Utils.toastShow(this@ShareActivity, "Send failure")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_share)

        val recyclerView: RecyclerView = this.findViewById(R.id.elementsRecView)
        // to avoid layout skipping
        recyclerView.adapter = ShareAdapter<Item>(Item::class, this.sendItem, arrayOf())
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemsButton: AppCompatButton = this.findViewById(R.id.itemsWatchButton)
        val locationsButton: AppCompatButton = this.findViewById(R.id.locationsWatchButton)
        val terminateButton: AppCompatButton = this.findViewById(R.id.terminateButton)

        itemsButton.setOnClickListener {
            val model: LocalItemsViewModel by viewModels {
                LocalItemsViewModelFactory(this.application, -1)
            }
            model.getLocalItems().observe(this, {
                recyclerView.adapter = ShareAdapter<Item>(Item::class, this.sendItem, it.toTypedArray())
            })
        }

        locationsButton.setOnClickListener {
            val model: LocationsViewModel by viewModels {
                DefaultAppViewModelFactory(this.application)
            }
            model.getPlaces().observe(this, {
                recyclerView.adapter = ShareAdapter<Place>(Place::class, this.sendPlace, it.toTypedArray())
            })
        }

        terminateButton.setOnClickListener {
            this.finish()
        }
    }

    private val sendItem: IndexedArrayCallback<Item> = {

    }

    private val sendPlace: IndexedArrayCallback<Place> = {
        //TODO
    }
}