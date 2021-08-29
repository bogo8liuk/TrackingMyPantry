package com.example.trackingmypantry.lib.credentials

import android.content.Context
import com.example.trackingmypantry.R

class CredentialsHandler {
    companion object {
        private const val EMAIL = "email"
        private const val PASSWORD = "password"

        fun setCredentials(context: Context, email: String, password: String) {
            val pref = context.getSharedPreferences(context.resources.getString(R.string.credentials), Context.MODE_PRIVATE)
            val editor = pref.edit()

            editor.putString(EMAIL, email).putString(PASSWORD, password).apply()
        }

        fun getEmail(context: Context): String? {
            val pref = context.getSharedPreferences(context.resources.getString(R.string.credentials), Context.MODE_PRIVATE)
            return pref.getString(EMAIL, null)
        }

        fun getPassword(context: Context): String? {
            val pref = context.getSharedPreferences(context.resources.getString(R.string.credentials), Context.MODE_PRIVATE)
            return pref.getString(PASSWORD, null)
        }

        fun clearCredentials(context: Context) {
            val pref = context.getSharedPreferences(context.resources.getString(R.string.credentials), Context.MODE_PRIVATE)
            val editor = pref.edit()

            editor.remove(EMAIL).remove(PASSWORD).apply()
        }
    }
}