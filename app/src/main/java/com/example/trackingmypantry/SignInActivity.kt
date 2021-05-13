package com.example.trackingmypantry

import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {
    private fun setEditText(editText: EditText, hint: Int, inputType: Int) {
        editText.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        editText.setEms(10)
        editText.setHint(hint)
        editText.setRawInputType(inputType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign) //TODO: change layout

        var insertingArea = findViewById<LinearLayout>(R.id.insertingArea)
        var usernameEditText = EditText(this)
        var passwordEditText = EditText(this)
        var signInButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.signButton)

        insertingArea.addView(usernameEditText)
        insertingArea.addView(passwordEditText)
        signInButton.setText(R.string.sign_in)
    }
}