package com.example.trackingmypantry.lib.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trackingmypantry.lib.TokenHandler
import com.example.trackingmypantry.lib.TokenType
import com.example.trackingmypantry.lib.net.HttpHandler
import com.example.trackingmypantry.lib.data.*
import org.json.JSONException
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
                    try {
                        val jsonRes = JSONObject(res)
                        TokenHandler.setToken(this.appContext, TokenType.SESSION, jsonRes.getString("token"))
                        it.value = rawResToItem(jsonRes)
                    } catch (exception: JSONException) {
                        it.value = mutableListOf(specialErrProduct(0, exception.message ?: "json exception"))
                    }
                },
                { statusCode, err ->
                    it.value = mutableListOf(specialErrProduct(statusCode, err))
                })
        }
    }

    private fun rawResToItem(res: JSONObject): List<Product> {
        val jsonProducts = res.getJSONArray("products")
        Log.e("pippo", jsonProducts.toString())
        Log.e("pippo", jsonProducts.length().toString())
        val products = mutableListOf<Product>()

        for (i in 0 until jsonProducts.length()) {
            val item = jsonProducts.getJSONObject(i)
            products.add(
                Product(
                    item.getString("name"),
                    item.getString("description"),
                    item.getString("barcode"),
                    item.getString("img"),  // TODO : verify
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