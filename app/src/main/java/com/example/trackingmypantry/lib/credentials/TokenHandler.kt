package com.example.trackingmypantry.lib.credentials

import android.content.Context

/**
 * Class that offers methods to handle shared preferences for tokens
 */
class TokenHandler() {
    companion object {
        private const val ACCESS = "accessToken"
        private const val SESSION = "sessionToken"

        val INEXISTENT_TOKEN: String? = null

        fun setToken(context: Context, type: TokenType, token: String) {
            val pref = Encrypted.sharedPrefs(context)
            val editor = pref.edit()

            if (type == TokenType.ACCESS)
                editor.putString(ACCESS, token)
            else
                editor.putString(SESSION, token)

            editor.apply()
        }

        fun removeToken(context: Context, type: TokenType) {
            val pref = Encrypted.sharedPrefs(context)
            val editor = pref.edit()
            if (type == TokenType.ACCESS)
                editor.remove(ACCESS)
            else
                editor.remove(SESSION)
            editor.apply()
        }

        /**
         * @warning: A call to this function with @param `type` as ACCESS remove the token, if @param
         * `remove` is true. If @param `type` has value SESSION, @param `remove` is ignored
         */
        fun getToken(context: Context, type: TokenType, remove: Boolean): String? {
            val pref = Encrypted.sharedPrefs(context)
            return if (type == TokenType.ACCESS) {
                val token = pref.getString(ACCESS, INEXISTENT_TOKEN)
                if (remove && token != null) {
                    val editor = pref.edit()
                    editor.remove(ACCESS)
                    editor.apply()
                }
                token
            } else
                pref.getString(SESSION, INEXISTENT_TOKEN)
        }
    }
}

enum class TokenType {
    ACCESS,
    SESSION
}