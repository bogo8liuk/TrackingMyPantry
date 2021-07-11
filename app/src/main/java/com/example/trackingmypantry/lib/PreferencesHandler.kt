package com.example.trackingmypantry.lib

import android.app.Activity
import android.content.Context

class TokenHandler() {
    companion object {
        private const val ACCESS = "accessToken"
        const val INEXISTENT_TOKEN = ""

        fun setAccessToken(from: Activity, token: String) {
            val pref = from.getPreferences(Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putString(ACCESS, token)
            editor.apply()
        }

        fun removeAccessToken(from: Activity) {
            val pref = from.getPreferences(Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putString(ACCESS, INEXISTENT_TOKEN)
            editor.apply()
        }

        fun getAccessToken(from: Activity): String {
            val pref = from.getPreferences(Context.MODE_PRIVATE)
            return pref.getString(ACCESS, INEXISTENT_TOKEN) ?: INEXISTENT_TOKEN
        }
    }
}