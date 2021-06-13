package com.example.trackingmypantry.lib

import android.content.Context
import android.os.Build
import android.util.JsonWriter
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.R
import org.json.JSONObject
import java.io.*

class Utils {
    companion object {
        fun setEditText(editText: EditText, hint: Int, inputType: Int) {
            editText.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            editText.setEms(10)
            editText.setHint(hint)
            editText.setRawInputType(inputType)
        }

        fun getLoginToken(context: Context): String {
            val input = File(context.filesDir,"../../../../../res/raw/log.json").readText(Charsets.UTF_8)
            val json = JSONObject(input).getJSONObject("log")
            // Safe cast: "accessToken" value is assured to be a string
            return json.get("accessToken") as String
        }

        fun getLoginStatus(context: Context): String {
            var input = File(context.filesDir,"../../../../../res/raw/log.json").readText(Charsets.UTF_8)
            var log = JSONObject(input)
            return log.get("status") as String
        }

        fun isLogged(context: Context): Boolean {
             return this.getLoginStatus(context) == "yes"
        }

        fun setToken(context: Context, token: String) {
            var jsonwriter = JsonWriter(
                OutputStreamWriter(File(context.filesDir, "../../../../../res/raw/log.json").outputStream())
            )
            val curStatus = this.getLoginStatus(context)
            jsonwriter.beginObject()
            jsonwriter.name("log")
            jsonwriter.beginObject()
            jsonwriter.name("status").value(curStatus)
            jsonwriter.name("accessToken").value(token)
            jsonwriter.endObject()
            jsonwriter.endObject()
        }

        fun setLogin(context: Context) {
            var jsonwriter = JsonWriter(
                OutputStreamWriter(File(context.filesDir, "../../../../../res/raw/log.json").outputStream())
            )
            val curToken = this.getLoginToken(context)
            jsonwriter.beginObject()
            jsonwriter.name("log")
            jsonwriter.beginObject()
            jsonwriter.name("status").value("yes")
            jsonwriter.name("accessToken").value(curToken)
            jsonwriter.endObject()
            jsonwriter.endObject()
        }

        fun setLogout(context: Context) {
            var jsonwriter = JsonWriter(
                OutputStreamWriter(File(context.filesDir, "../../../../../res/raw/log.json").outputStream())
            )
            val curToken = this.getLoginToken(context)
            jsonwriter.beginObject()
            jsonwriter.name("log")
            jsonwriter.beginObject()
            jsonwriter.name("status").value("yes")
            jsonwriter.name("accessToken").value(curToken)
            jsonwriter.endObject()
            jsonwriter.endObject()
        }

        fun toastShow(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    class ResultCode {
        companion object {
            const val NETWORK_ERR = 2
        }
    }
}