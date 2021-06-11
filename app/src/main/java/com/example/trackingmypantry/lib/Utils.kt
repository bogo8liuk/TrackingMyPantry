package com.example.trackingmypantry.lib

import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast

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

        fun toastShow(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}