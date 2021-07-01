package com.example.trackingmypantry.lib

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trackingmypantry.db.entities.Item
import org.json.JSONObject
import java.sql.Date

class ReceivedItemsViewModel(app: Application, barcode: String, accessToken: String): AndroidViewModel(app) {
    /* No memory leaks: there is only one Application instance when app is running. */
    private val appContext = app.applicationContext

    private val receivedItems: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>().also {
            HttpHandler.serviceGetProduct(
                appContext,
                barcode,
                accessToken,
                { res ->
                    it.value = rawResToItem(res)
                },
                { statusCode, err ->
                    it.value = Utils.special_err_item(statusCode, err)
                })
        }
    }

    private fun rawResToItem(res: String): List<Item>? {
        val json = JSONObject(res)  // TODO: handle a jsonexception
        val jsonItems = json.getJSONArray("products")
        val items = mutableListOf<Item>()

        for (i in 0 until jsonItems.length()) {
            val item = jsonItems.getJSONObject(i)
            items.add(
                Item( // TODO: consider to use another type instead of Item
                    Utils.SPECIAL_ID,
                    item.getString("barcode"),
                    item.getString("name"),
                    item.getString("description"),
                    item.getString("img"),  // TODO : verify
                    1,
                    Date(-1),
                    null
                )
            )
        }

        return items
    }

    fun getReceivedItems(): LiveData<List<Item>> {
        return receivedItems
    }
}