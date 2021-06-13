package com.example.trackingmypantry.lib

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.util.HashMap

/* This offers the methods to make the requests to the web service. */
class HttpHandler() {
    companion object {
        private const val DOMAIN = "https://lam21.modron.network"
        private const val AUTH_PATH = "/auth/login"
        private const val REGISTER_PATH = "/users"
        private const val PRODUCT_PATH = "/products"
        private const val VOTE_PATH = "/votes"

        private const val TESTING_MODE = true

        fun serviceAuthenticate(
            context: Context,
            email: String,
            password: String,
            successCallback: (JSONObject) -> Unit,
            errorCallback: (String) -> Unit) {
            val req = object: JsonObjectRequest(
                Request.Method.POST,
                "$DOMAIN$AUTH_PATH",
                JSONObject("{ \"email\": \"$email\", \"password\": \"$password\"}"),
                { res -> successCallback(res) },
                { err -> errorCallback(err.toString())
                }
            ) {

            }
            ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
        }

        fun serviceRegister(
            context: Context,
            username: String,
            email: String,
            password: String,
            successCallback: (JSONObject) -> Unit,
            errorCallback: (String) -> Unit) {
            val req = JsonObjectRequest(
                Request.Method.POST,
                "$DOMAIN$REGISTER_PATH",
                JSONObject("{ \"username\": \"$username\", \"email\": \"$email\", \"password\": \"$password\"}"),
                { res -> successCallback(res) },
                { err -> errorCallback(err.toString()) }
            )
            ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
        }

        fun serviceGetProduct(
            context: Context,
            barcode: String,
            accessToken: String,
            successCallback: (String) -> Unit,
            errorCallback: (String) -> Unit) {
            /* object expression to override getHeaders() in order to add custom headers. */
            val req = object: StringRequest(
                Method.GET,
                "$DOMAIN$PRODUCT_PATH?barcode=$barcode",
                { res -> successCallback(res) },
                { err -> errorCallback(err.toString()) }
            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = super.getHeaders()
                    headers.put("Authorization", "Bearer $accessToken")
                    return headers
                }
            }
            ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
        }

        fun serviceDescribeProduct(
            context: Context,
            token: String /* TODO: necessary? */,
            name: String,
            description: String,
            barcode: String,
            successCallback: (JSONObject) -> Unit,
            errorCallback: (String) -> Unit
        ) {
            val req = JsonObjectRequest(
                Request.Method.POST,
                "$DOMAIN$PRODUCT_PATH",
                JSONObject(
                    "{ \"token\": \"$token\", " +
                            "\"name\": \"$name\"," +
                            "\"description\": \"$description\"," +
                            "\"barcode\": \"$barcode\"," +
                            "\"test\": \"$TESTING_MODE\" }"
                ),
                { res -> successCallback(res) },
                { err -> errorCallback(err.toString()) }
            )
            ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
        }

        fun serviceDeleteProduct(
            context: Context,
            id: String,
            successCallback: (String) -> Unit,
            errorCallback: (String) -> Unit) {
            val req = StringRequest(
                Request.Method.DELETE,
                "$DOMAIN$PRODUCT_PATH/$id",
                { res -> successCallback(res) },
                { err -> errorCallback(err.toString()) }
            )
            ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
        }

        fun serviceVoteProduct(
            context: Context,
            token: String /* TODO: necessary? */,
            rating: Int,
            id: String,
            successCallback: (JSONObject) -> Unit,
            errorCallback: (String) -> Unit
        ) {
            val req = JsonObjectRequest(
                Request.Method.POST,
                "$DOMAIN$VOTE_PATH",
                JSONObject("{ \"token\": \"$token\", \"rating\": \"$rating\", \"productId\": \"$id\"}"),
                { res -> successCallback(res) },
                { err -> errorCallback(err.toString()) }
            )
            ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
        }
    }
}