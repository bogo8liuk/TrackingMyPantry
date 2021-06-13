package com.example.trackingmypantry.lib

import android.content.Context
import android.os.Build
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.R
import org.json.JSONObject
import java.io.File
import java.io.InputStream

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

        fun isLogged(context: Context): Boolean {
            var input = File(context.filesDir,"../../../../../res/raw/log.json").readText(Charsets.UTF_8)
            var log = JSONObject(input)
            return log.get("status") == "yes"
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