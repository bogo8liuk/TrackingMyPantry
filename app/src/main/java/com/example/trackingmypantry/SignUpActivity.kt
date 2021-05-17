package com.example.trackingmypantry

import android.os.Bundle
import android.text.InputType
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {
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
        var emailEditText = EditText(this)
        var passwordEditText = EditText(this)
        var signUpButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.signButton)

        // Attaching views to linearLayout
        insertingArea.addView(usernameEditText)
        insertingArea.addView(emailEditText)
        insertingArea.addView(passwordEditText)
        signUpButton.setText(R.string.sign_up)

        setEditText(usernameEditText, R.string.username_hint, InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
        setEditText(emailEditText, R.string.email_hint, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        setEditText(passwordEditText, R.string.password_hint, InputType.TYPE_TEXT_VARIATION_PASSWORD)
    }


}