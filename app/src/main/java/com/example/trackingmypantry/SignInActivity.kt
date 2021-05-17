package com.example.trackingmypantry

import android.os.Bundle
import android.text.InputType
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
        this.setContentView(R.layout.activity_sign)

        var insertingArea = findViewById<LinearLayout>(R.id.inserting_area)
        var usernameEditText = EditText(this)
        var passwordEditText = EditText(this)
        var signInButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.signButton)

        insertingArea.addView(usernameEditText)
        insertingArea.addView(passwordEditText)
        signInButton.setText(R.string.sign_in)

        setEditText(usernameEditText, R.string.username_hint, InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
        setEditText(passwordEditText, R.string.password_hint, InputType.TYPE_TEXT_VARIATION_PASSWORD)
    }
}