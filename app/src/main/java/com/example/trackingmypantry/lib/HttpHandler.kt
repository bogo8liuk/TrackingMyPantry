package com.example.trackingmypantry.lib

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject

/* This offers the methods to make the requests to the web service. */
class HttpHandler() {
    private val DOMAIN = "https://lam21.modron.network"
    private val AUTH_PATH = "/auth/login"
    private val REGISTER_PATH = "/users"
    private val PRODUCT_PATH = "/products"
    private val VOTE_PATH = "/votes"

    private val TESTING_MODE = true

    fun serviceAuthenticate(context: Context, email: String, password: String) {
        val req = JsonObjectRequest(
            Request.Method.POST,
            "$DOMAIN$AUTH_PATH",
            JSONObject("{ \"email\": \"$email\", \"password\": \"$password\"}"),
            { res -> HttpResponse.OK },
            { err ->
                Log.e("Network error", err.toString())
                HttpResponse.ERROR
            }
        )
        ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
    }

    fun serviceRegister(context: Context, username: String, email: String, password: String) {
        val req = JsonObjectRequest(
            Request.Method.POST,
            "$DOMAIN$REGISTER_PATH",
            JSONObject("{ \"username\": \"$username\", \"email\": \"$email\", \"password\": \"$password\"}"),
            { res -> HttpResponse.OK },
            { err ->
                Log.e("Network error", err.toString())
                HttpResponse.ERROR
            }
        )
        ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
    }

    fun serviceGetProduct(context: Context, barcode: String) {
        val req = StringRequest(
            Request.Method.GET,
            "$DOMAIN$PRODUCT_PATH?barcode=$barcode",
            { res -> JSONObject(res)},
            { err ->
                Log.e("Network error", err.toString())
                HttpResponse.ERROR
            }
        )
        ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
    }

    fun serviceDescribeProduct(context: Context, token: String /* TODO: necessary? */, name: String, description: String, barcode: String ) {
        val req = JsonObjectRequest(
            Request.Method.POST,
            "$DOMAIN$PRODUCT_PATH",
            JSONObject("{ \"token\": \"$token\", " +
                    "\"name\": \"$name\"," +
                    "\"description\": \"$description\"," +
                    "\"barcode\": \"$barcode\"," +
                    "\"test\": \"$TESTING_MODE\" }"),
            { res -> HttpResponse.OK },
            { err ->
                Log.e("Network error", err.toString())
                HttpResponse.ERROR
            }
        )
        ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
    }

    fun serviceDeleteProduct(context: Context, id: String) {
        val req = StringRequest(
            Request.Method.DELETE,
            "$DOMAIN$PRODUCT_PATH/$id",
            { res -> HttpResponse.OK },
            { err ->
                Log.e("Network error", err.toString())
                HttpResponse.ERROR
            }
        )
        ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
    }

    fun serviceVoteProduct(context: Context, token: String /* TODO: necessary? */, rating: Int, id: String) {
        val req = JsonObjectRequest(
            Request.Method.POST,
            "$DOMAIN$VOTE_PATH",
            JSONObject("{ \"token\": \"$token\", \"rating\": \"$rating\", \"productId\": \"$id\"}"),
            { res -> HttpResponse.OK },
            { err ->
                Log.e("Network error", err.toString())
                HttpResponse.ERROR
            }
        )
        ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
    }
}