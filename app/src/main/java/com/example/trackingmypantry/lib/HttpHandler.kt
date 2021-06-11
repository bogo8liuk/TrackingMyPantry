package com.example.trackingmypantry.lib

import android.content.Context
import com.android.volley.toolbox.Volley

/* This offers the methods to make the requests to the web service. */
class HttpHandler(context : Context) {
    private val DOMAIN = "https://lam21.modron.network"
    private val AUTH_RES = "/auth/login"
    private val USER_RES = "/users"
    private val PRODUCT_RES = "/products"
    private val VOTE_RES = "/votes"

    fun serviceAuthenticate() {
        
    }
}