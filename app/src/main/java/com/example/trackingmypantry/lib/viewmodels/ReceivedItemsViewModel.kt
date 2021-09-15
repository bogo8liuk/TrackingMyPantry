package com.example.trackingmypantry.lib.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trackingmypantry.lib.credentials.TokenHandler
import com.example.trackingmypantry.lib.credentials.TokenType
import com.example.trackingmypantry.lib.connectivity.net.HttpHandler
import com.example.trackingmypantry.lib.data.*
import org.json.JSONException
import org.json.JSONObject

class ReceivedItemsViewModel(app: Application, barcode: String): AndroidViewModel(app) {
    /* No memory leaks: there is only one Application instance when app is running. */
    private val appContext = app.applicationContext

    private val receivedItems: MutableLiveData<List<Product>> by lazy {
        MutableLiveData<List<Product>>().also {
            HttpHandler.retryOnFailure(
                HttpHandler.RequestType.GET,
                appContext,
                { res ->
                    try {
                        val jsonRes = JSONObject(res as String)
                        TokenHandler.setToken(this.appContext, TokenType.SESSION, jsonRes.getString("token"))
                        it.value = rawResToItem(jsonRes)
                    } catch (exception: JSONException) {
                        it.value = mutableListOf(specialErrProduct(0, exception.message ?: "json exception"))
                    }
                },
                { statusCode, err ->
                    it.value = mutableListOf(specialErrProduct(statusCode, err))
                },
                getParams = HttpHandler.Companion.GetParams(barcode)
            )
        }
    }

    private fun rawResToItem(res: JSONObject): List<Product> {
        val jsonProducts = res.getJSONArray("products")
        val products = mutableListOf<Product>()

        for (i in 0 until jsonProducts.length()) {
            val item = jsonProducts.getJSONObject(i)
            products.add(
                Product(
                    item.getString("name"),
                    item.getString("description"),
                    item.getString("barcode"),
                    item.getString("img"),
                    item.getString("id")
                )
            )
        }

        return products
    }

    fun getReceivedItems(): LiveData<List<Product>> {
        return receivedItems
    }
}