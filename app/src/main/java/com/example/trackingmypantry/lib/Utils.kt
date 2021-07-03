package com.example.trackingmypantry.lib

import android.content.Context
import android.widget.Toast
import com.example.trackingmypantry.lib.data.Product
import org.json.JSONObject

class Utils {
    companion object {
        val logFileName = "log.json"

        /*
        * WARNING: Do not call this function several times: since it reads a file, it can decrease
        * app's performance. */
        fun getLoginToken(context: Context): String? {
            val input = context.openFileInput(logFileName).bufferedReader().useLines { lines ->
                lines.fold("") { accumulated, value ->
                    "$accumulated\n$value"
                }
            }
            val json = JSONObject(input)
            // Safe cast: "accessToken" value is assured to be a string
            val res = json.get("accessToken") as String
            val status = json.get("accessToken") as String
            return if (res == "null" && status != "yes") null else res
        }

        /*
        * WARNING: Do not call this function several times: since it reads a file, it can decrease
        * app's performance.
        */
        fun getLoginStatus(context: Context): String {
            val input = context.openFileInput(logFileName).bufferedReader().useLines { lines ->
                lines.fold("") { accumulated, value ->
                    "$accumulated\n$value"
                }
            }
            var log = JSONObject(input)
            return log.get("status") as String
        }

        /*
        * WARNING: parameter @loginStatus should have been got by getLoginStatus() method. It is not
        * directly called inside this function because it could cause the worsening of app's
        * performance.
        */
        fun isLogged(loginStatus: String): Boolean {
             return loginStatus == "yes"
        }

        fun setToken(context: Context, token: String) {
            context.openFileOutput(logFileName, Context.MODE_PRIVATE).use {
                it.write(
                    """
{
    "status": "yes",
    "accessToken": "$token"
}
                """.trimIndent().encodeToByteArray()
                )
            }
        }

        fun setLogout(context: Context) {
            context.openFileOutput(logFileName, Context.MODE_PRIVATE).use {
                it.write(
                    """
{
    "status": "no",
    "accessToken": "null"
}
                """.trimIndent().encodeToByteArray()
                )
            }
        }

        fun toastShow(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}