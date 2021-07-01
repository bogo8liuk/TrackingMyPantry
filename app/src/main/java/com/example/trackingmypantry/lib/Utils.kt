package com.example.trackingmypantry.lib

import android.content.Context
import android.os.Build
import android.util.JsonWriter
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.R
import com.example.trackingmypantry.db.entities.Item
import org.json.JSONObject
import java.io.*
import java.sql.Date

class Utils {
    companion object {
        val DEFAULT_BARCODE = "000000000000"
        val ERR_BARCODE = "err"
        val SPECIAL_ID = -1L
        val logFileName = "log.json"

        fun special_err_item(statusCode: Int, err: String) = List(1) { _ ->
            Item(
                statusCode.toLong(),
                ERR_BARCODE,
                "err",
                err,
                null,
                -1,
                Date(-1),
                null
            )
        }

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

    class ResultCode {
        companion object {
            const val NETWORK_ERR = 2
            const val EXISTENT_USER = 3
            const val EXPIRED_TOKEN = 4
        }
    }
}