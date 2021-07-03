package com.example.trackingmypantry.lib.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trackingmypantry.db.entities.Item
import com.example.trackingmypantry.lib.net.HttpHandler
import com.example.trackingmypantry.lib.data.Product
import com.example.trackingmypantry.lib.Utils
import org.json.JSONObject

class ReceivedItemsViewModel(app: Application, barcode: String, accessToken: String): AndroidViewModel(app) {
    /* No memory leaks: there is only one Application instance when app is running. */
    private val appContext = app.applicationContext

    private val receivedItems: MutableLiveData<List<Product>> by lazy {
        MutableLiveData<List<Product>>().also {
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

    private fun rawResToItem(res: String): List<Product> {
        val json = JSONObject(res)  // TODO: handle a jsonexception
        val jsonProducts = json.getJSONArray("products")
        val products = mutableListOf<Product>()

        for (i in 0 until jsonProducts.length()) {
            val item = jsonProducts.getJSONObject(i)
            products.add(
                Product(
                    item.getString("name"),
                    item.getString("description"),
                    item.getString("barcode"),
                    item.getString("img")  // TODO : verify
                )
            )
        }

        return products
    }

    fun getReceivedItems(): LiveData<List<Item>> {
        return receivedItems
    }
}