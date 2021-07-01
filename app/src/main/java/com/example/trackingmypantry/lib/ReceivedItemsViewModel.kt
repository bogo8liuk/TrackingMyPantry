package com.example.trackingmypantry.lib

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trackingmypantry.db.entities.Item

class ReceivedItemsViewModel(app: Application, barcode: String, accessToken: String): AndroidViewModel(app) {
    /* No memory leaks: there is only one Application instance when app is running. */
    private val appContext = app.applicationContext
    private val barcode = barcode
    private val accessToken = accessToken

    private val receivedItems: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>().also {
            HttpHandler.serviceGetProduct(
                appContext,
                barcode,
                accessToken,
                { res ->
                    it.value = res  //TODO
                },
                { statusCode, err ->
                    it.value = err  // TODO
                })
        }
    }

    fun getReceivedItems(): LiveData<List<Item>> {
        return receivedItems
    }
}