package com.example.trackingmypantry.lib

import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.R

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

        fun setButtonForMain(button: AppCompatButton, background: Int) {
            button.setBackgroundResource(background)
            /* TODO: finish */
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