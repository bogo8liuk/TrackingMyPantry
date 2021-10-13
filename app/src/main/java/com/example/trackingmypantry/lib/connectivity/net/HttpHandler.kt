package com.example.trackingmypantry.lib.connectivity.net

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.credentials.CredentialsHandler
import com.example.trackingmypantry.lib.credentials.TokenHandler
import com.example.trackingmypantry.lib.credentials.TokenType
import org.json.JSONObject

/**
 * This offers the methods to make the requests to the web service.
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
            val req = JsonObjectRequest(
                Request.Method.POST,
                "$DOMAIN$AUTH_PATH",
                JSONObject("{ \"email\": \"$email\", \"password\": \"$password\"}"),
                { res ->
                    TokenHandler.setToken(context, TokenType.ACCESS, res.getString(
                        ACCESS_TOKEN_FIELD
                    ))
                    CredentialsHandler.setCredentials(context, email, password)
                    successCallback(res) },
                { err ->
                    if (err.networkResponse == null) {
                        errorCallback(
                            503,    // Service unavailable
                            "Service unavailable"
                        )
                    } else {
                        errorCallback(
                            err.networkResponse.statusCode,
                            JSONObject(String(err.networkResponse.data)).optString("message")
                        )
                    }
                }
            )
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
                { err ->
                    if (err.networkResponse == null) {
                        errorCallback(
                            503,    // Service unavailable
                            "Service unavailable"
                        )
                    } else {
                        errorCallback(
                            err.networkResponse.statusCode,
                            JSONObject(String(err.networkResponse.data)).optString("message")
                        )
                    }
                }
            )
            ReqQueueSingleton.getInstance(context.applicationContext).addRequest(req)
        }

        fun serviceGetProduct(
            context: Context,
            barcode: String,
            successCallback: (String) -> Unit,
            errorCallback: (Int, String) -> Unit): Boolean {
            val accessToken = TokenHandler.getToken(context, TokenType.ACCESS, false)
            if (accessToken == TokenHandler.INEXISTENT_TOKEN) {
                return false
            }

            /* object expression to override getHeaders() in order to add custom headers. */
            val req = object: StringRequest(
                Request.Method.GET,
                "$DOMAIN$PRODUCT_PATH?barcode=$barcode",
                { res -> successCallback(res) },
                { err ->
                    if (err.networkResponse == null) {
                        errorCallback(
                            503,    // Service unavailable
                            "Service unavailable"
                        )
                    } else {
                        errorCallback(
                            err.networkResponse.statusCode,
                            JSONObject(String(err.networkResponse.data)).optString("message")
                        )
                    }
                }
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
            base64: String?,
            successCallback: (JSONObject) -> Unit,
            errorCallback: (Int, String) -> Unit
        ): Boolean {
            val accessToken = TokenHandler.getToken(context, TokenType.ACCESS, false)
            if (accessToken == TokenHandler.INEXISTENT_TOKEN) {
                return false
            }
            val sessionToken = TokenHandler.getToken(context, TokenType.SESSION, false)

            val req = object: JsonObjectRequest(
                Request.Method.POST,
                "$DOMAIN$PRODUCT_PATH",
                if (base64 != null)
                    JSONObject(
                    "{ \"token\": \"$sessionToken\", " +
                            "\"name\": \"$name\", " +
                            "\"description\": \"$description\", " +
                            "\"barcode\": \"$barcode\", " +
                            "\"img\": \"$base64\", " +
                            "\"test\": $TESTING_MODE }"
                    )
                else
                    JSONObject(
                        "{ \"token\": \"$sessionToken\", " +
                            "\"name\": \"$name\", " +
                            "\"description\": \"$description\", " +
                            "\"barcode\": \"$barcode\", " +
                            "\"test\": $TESTING_MODE }"
                    ),
                { res -> successCallback(res) },
                { err ->
                    if (err.networkResponse == null) {
                        errorCallback(
                            503,    // Service unavailable
                            "Service unavailable"
                        )
                    } else {
                        errorCallback(
                            err.networkResponse.statusCode,
                            JSONObject(String(err.networkResponse.data)).optString("message")
                        )
                    }
                }
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

        /*fun serviceDeleteProduct(
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
        }*/

        fun serviceVoteProduct(
            context: Context,
            rating: Int,
            id: String,
            successCallback: (JSONObject) -> Unit,
            errorCallback: (Int, String) -> Unit
        ): Boolean {
            val accessToken = TokenHandler.getToken(context, TokenType.ACCESS, true)
            if (accessToken == TokenHandler.INEXISTENT_TOKEN) {
                return false
            }
            val sessionToken = TokenHandler.getToken(context, TokenType.SESSION, false)

            val req = object: JsonObjectRequest(
                Request.Method.POST,
                "$DOMAIN$VOTE_PATH",
                JSONObject("{ \"token\": \"$sessionToken\", \"rating\": $rating, \"productId\": \"$id\" }"),
                { res -> successCallback(res) },
                { err ->
                    if (err.networkResponse == null) {
                        errorCallback(
                            503,    // Service unavailable
                            "Service unavailable"
                        )
                    } else {
                        errorCallback(
                            err.networkResponse.statusCode,
                            JSONObject(String(err.networkResponse.data)).optString("message")
                        )
                    }
                }
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

        data class GetParams(
            val barcode: String
        )

        data class VoteParams(
            val rating: Int,
            val id: String
        )

        data class DescribeParams(
            val name: String,
            val description: String,
            val barcode: String,
            val image: String?
        )

        /**
         * It calls one between serviceGetProducts(), serviceVoteProduct() and serviceDescribeProduct()
         * and it handles the case the call returns false, calling serviceAuthenticate() and trying
         * to recall the former method. Thus, it provides a more robust way than boolean-returning
         * methods to deal with the web service.
         * @warning A null pointer exception is not handled.
         */
        fun retryOnFailure(
            type: RequestType,
            context: Context,
            success: (Any) -> Unit,
            error: (Int, String) -> Unit,
            getParams: GetParams? = null,
            voteParams: VoteParams? = null,
            describeParams: DescribeParams? = null
        ) {
            when (type) {
                RequestType.GET -> {
                    val barcode = getParams!!.barcode

                    if (!serviceGetProduct(context, barcode, success, error)) {
                        val email = CredentialsHandler.getEmail(context)
                        val password = CredentialsHandler.getPassword(context)

                        if (email == null || password == null) {
                            Log.e("Unexpected", "Credentials email and password should have already been set")
                            Utils.toastShow(context, "Unable to get products")
                        } else {
                            serviceAuthenticate(
                                context,
                                email,
                                password,
                                { _ ->
                                    if (!serviceGetProduct(context, barcode, success, error)) {
                                        Log.e("Unexpected", "Get request returning false multiple times")
                                        Utils.toastShow(context, "Unable to get products")
                                    }
                                },
                                { _, _ ->
                                    Log.e(
                                        "Unexpected",
                                        "Authentication should not have problems"
                                    )
                                    Utils.toastShow(context, "Unable to get products")
                                }
                            )
                        }
                    }
                }

                RequestType.VOTE -> {
                    val rating = voteParams!!.rating
                    val id = voteParams.id

                    if (!serviceVoteProduct(context, rating, id, success, error)) {
                        val email = CredentialsHandler.getEmail(context)
                        val password = CredentialsHandler.getPassword(context)

                        if (email == null || password == null) {
                            Log.e("Unexpected", "Credentials email and password should have already been set")
                            Utils.toastShow(context, "Unable to vote the product")
                        } else {
                            serviceAuthenticate(
                                context,
                                email,
                                password,
                                { _ ->
                                    if (!serviceVoteProduct(context, rating, id, success, error)) {
                                        Log.e("Unexpected", "Post request returning false multiple times")
                                        Utils.toastShow(context, "Unable to vote the product")
                                    }
                                },
                                { _, _ ->
                                    Log.e(
                                        "Unexpected",
                                        "Authentication should not have problems"
                                    )
                                    Utils.toastShow(context, "Unable to vote the product")
                                }
                            )
                        }
                    }
                }

                RequestType.DESCRIBE -> {
                    val name = describeParams!!.name
                    val description = describeParams.description
                    val barcode = describeParams.barcode
                    val image = describeParams.image

                    if (!serviceDescribeProduct(context, name, description, barcode, image, success, error)) {
                        val email = CredentialsHandler.getEmail(context)
                        val password = CredentialsHandler.getPassword(context)

                        if (email == null || password == null) {
                            Log.e("Unexpected", "Credentials email and password should have already been set")
                            Utils.toastShow(context, "Unable to send the description")
                        } else {
                            serviceAuthenticate(
                                context,
                                email,
                                password,
                                { _ ->
                                    if (!serviceDescribeProduct(context, name, description, barcode, image, success, error)) {
                                        Log.e("Unexpected", "Post request returning false multiple times")
                                        Utils.toastShow(context, "Unable to send the description")
                                    }
                                },
                                { _, _ ->
                                    Log.e(
                                        "Unexpected",
                                        "Authentication should not have problems"
                                    )
                                    Utils.toastShow(context, "Unable to send the description")
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    enum class RequestType {
        GET,
        VOTE,
        DESCRIBE
    }
}