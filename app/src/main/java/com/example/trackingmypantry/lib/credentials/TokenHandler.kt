package com.example.trackingmypantry.lib.credentials

import android.content.Context
import com.example.trackingmypantry.R

/* Class that offers methods to handle shared preferences for tokens */
class TokenHandler() {
    companion object {
        private const val ACCESS = "accessToken"
        private const val SESSION = "sessionToken"

        const val INEXISTENT_TOKEN = "__IT"

        fun setToken(context: Context, type: TokenType, token: String) {
            val pref = context.getSharedPreferences(context.resources.getString(R.string.accessToken), Context.MODE_PRIVATE)
            val editor = pref.edit()

            if (type == TokenType.ACCESS)
                editor.putString(ACCESS, token)
            else
                editor.putString(SESSION, token)

            editor.apply()
        }

        fun removeToken(context: Context, type: TokenType) {
            val pref = context.getSharedPreferences(context.resources.getString(R.string.accessToken), Context.MODE_PRIVATE)
            val editor = pref.edit()
            if (type == TokenType.ACCESS)
                editor.putString(ACCESS, INEXISTENT_TOKEN)
            else
                editor.putString(SESSION, INEXISTENT_TOKEN)
            editor.apply()
        }

        /**
         * @warning: A call to this function with @param `type` as ACCESS remove the token, if @param
         * `remove` is true.
         */
        fun getToken(context: Context, type: TokenType, remove: Boolean): String {
            val pref = context.getSharedPreferences(context.resources.getString(R.string.accessToken), Context.MODE_PRIVATE)
            return if (type == TokenType.ACCESS) {
                val token = pref.getString(ACCESS, INEXISTENT_TOKEN) ?: INEXISTENT_TOKEN
                if (remove) {
                    val editor = pref.edit()
                    editor.putString(ACCESS, INEXISTENT_TOKEN)
                    editor.apply()
                }
                token
            } else
                pref.getString(SESSION, INEXISTENT_TOKEN) ?: INEXISTENT_TOKEN
        }
    }
}

enum class TokenType {
    ACCESS,
    SESSION
}