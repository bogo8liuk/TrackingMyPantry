package com.example.trackingmypantry.lib.net

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.trackingmypantry.lib.credentials.TokenHandler
import com.example.trackingmypantry.lib.credentials.TokenType
import org.json.JSONObject

/* This offers the methods to make the requests to the web service. */
/**
 * The methods having a boolean returning type, have the following semantics:
 * @return true if the http request is really carried out, false if the request is not
 * carried out because of an inexistent access token. Call serviceRegister() to renew
 * the access token.
 */
class HttpHandler() {
    companion object {
        private const val DOMAIN = "https://lam21.modron.network"
        private const val AUTH_PATH = "/auth/login"
        private const val REGISTER_PATH = "/users"
        private const val PRODUCT_PATH = "/products"
        private const val VOTE_PATH = "/votes"

        private const val TESTING_MODE = true

        const val ACCESS_TOKEN_FIELD = "accessToken"

        fun serviceAuthenticate(
            context: Context,
            email: String,
            password: String,
            successCallback: (JSONObject) -> Unit,
            errorCallback: (Int, String) -> Unit) {
            val req = object: JsonObjectRequest(
                Request.Method.POST,
                "$DOMAIN$AUTH_PATH",
                JSONObject("{ \"email\": \"$email\", \"password\": \"$password\"}"),
                { res -> successCallback(res) },
                { err -> errorCallback(
                    err.networkResponse.statusCode,
                    JSONObject(String(err.networkResponse.data)).optString("message")
                )}
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
            errorCallback: (Int, String) -> Unit) {
            val req = JsonObjectRequest(
                Request.Method.POST,
                "$DOMAIN$REGISTER_PATH",
                JSONObject("{ \"username\": \"$username\", \"email\": \"$email\", \"password\": \"$password\"}"),
                { res -> successCallback(res) },
                { err -> errorCallback(
                    err.networkResponse.statusCode,
                    JSONObject(String(err.networkResponse.data)).optString("message")
                )}
            )
            ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
        }

        fun serviceGetProduct(
            context: Context,
            barcode: String,
            successCallback: (String) -> Unit,
            errorCallback: (Int, String) -> Unit): Boolean {
            val accessToken = TokenHandler.getToken(context, TokenType.ACCESS)
            if (accessToken == TokenHandler.INEXISTENT_TOKEN) {
                return false
            }

            /* object expression to override getHeaders() in order to add custom headers. */
            val req = object: StringRequest(
                Request.Method.GET,
                "$DOMAIN$PRODUCT_PATH?barcode=$barcode",
                { res -> successCallback(res) },
                { err ->
                    errorCallback(
                    err.networkResponse.statusCode,
                    JSONObject(String(err.networkResponse.data)).optString("message", "No message")
                )}
            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>(super.getHeaders())
                    headers["Authorization"] = "Bearer $accessToken"
                    return headers
                }
            }
            ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
            return true
        }

        fun serviceDescribeProduct(
            context: Context,
            name: String,
            description: String,
            barcode: String,
            successCallback: (JSONObject) -> Unit,
            errorCallback: (Int, String) -> Unit
        ): Boolean {
            val accessToken = TokenHandler.getToken(context, TokenType.ACCESS)
            if (accessToken == TokenHandler.INEXISTENT_TOKEN) {
                return false
            }
            val sessionToken = TokenHandler.getToken(context, TokenType.ACCESS)

            val req = object: JsonObjectRequest(
                Request.Method.POST,
                "$DOMAIN$PRODUCT_PATH",
                JSONObject(
                    "{ \"token\": \"$sessionToken\", " +
                            "\"name\": \"$name\"," +
                            "\"description\": \"$description\"," +
                            "\"barcode\": \"$barcode\"," +
                            "\"test\": \"$TESTING_MODE\" }"
                ),
                { res -> successCallback(res) },
                { err -> errorCallback(
                    err.networkResponse.statusCode,
                    JSONObject(String(err.networkResponse.data)).optString("message")
                )}
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>(super.getHeaders())
                    headers["Authorization"] = "Bearer $accessToken"
                    return headers
                }
            }
            ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
            return true
        }

        fun serviceDeleteProduct(
            context: Context,
            id: String,
            successCallback: (String) -> Unit,
            errorCallback: (Int, String) -> Unit) {
            val req = StringRequest(
                Request.Method.DELETE,
                "$DOMAIN$PRODUCT_PATH/$id",
                { res -> successCallback(res) },
                { err -> errorCallback(
                    err.networkResponse.statusCode,
                    JSONObject(String(err.networkResponse.data)).optString("message")
                )}
            )
            ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
        }

        fun serviceVoteProduct(
            context: Context,
            rating: Int,
            id: String,
            successCallback: (JSONObject) -> Unit,
            errorCallback: (Int, String) -> Unit
        ): Boolean {
            val accessToken = TokenHandler.getToken(context, TokenType.ACCESS)
            if (accessToken == TokenHandler.INEXISTENT_TOKEN) {
                return false
            }
            val sessionToken = TokenHandler.getToken(context, TokenType.ACCESS)

            val req = object: JsonObjectRequest(
                Request.Method.POST,
                "$DOMAIN$VOTE_PATH",
                JSONObject("{ \"token\": \"$sessionToken\", \"rating\": $rating, \"productId\": \"$id\" }"),
                { res -> successCallback(res) },
                { err -> errorCallback(
                    err.networkResponse.statusCode,
                    JSONObject(String(err.networkResponse.data)).optString("message")
                )}
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>(super.getHeaders())
                    headers["Authorization"] = "Bearer $accessToken"
                    return headers
                }
            }
            ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
            return true
        }
    }
}