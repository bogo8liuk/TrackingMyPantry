package com.example.trackingmypantry.lib

import android.app.Activity
import android.content.Context

/* Class that offers methods to handle shared preferences for tokens */
class TokenHandler() {
    companion object {
        private const val ACCESS = "accessToken"
        private const val SESSION = "sessionToken"
        const val INEXISTENT_TOKEN = ""

        fun setToken(from: Activity, type: TokenType, token: String) {
            val pref = from.getPreferences(Context.MODE_PRIVATE)
            val editor = pref.edit()
            if (type == TokenType.ACCESS)
                editor.putString(ACCESS, token)
            else
                editor.putString(SESSION, token)
            editor.apply()
        }

        fun removeToken(from: Activity, type: TokenType) {
            val pref = from.getPreferences(Context.MODE_PRIVATE)
            val editor = pref.edit()
            if (type == TokenType.ACCESS)
                editor.putString(ACCESS, INEXISTENT_TOKEN)
            else
                editor.putString(SESSION, INEXISTENT_TOKEN)
            editor.apply()
        }

        fun getToken(from: Activity, type: TokenType): String {
            val pref = from.getPreferences(Context.MODE_PRIVATE)
            return if (type == TokenType.ACCESS)
                pref.getString(ACCESS, INEXISTENT_TOKEN) ?: INEXISTENT_TOKEN
            else
                pref.getString(SESSION, INEXISTENT_TOKEN) ?: INEXISTENT_TOKEN
        }
    }
}

enum class TokenType {
    ACCESS,
    SESSION
}