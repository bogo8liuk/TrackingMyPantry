package com.example.trackingmypantry

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.trackingmypantry.lib.HttpHandler
import com.example.trackingmypantry.lib.Utils

class SignUpActivity : AppCompatActivity() {
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

        Utils.setEditText(usernameEditText, R.string.username_hint, InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
        Utils.setEditText(emailEditText, R.string.email_hint, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        Utils.setEditText(passwordEditText, R.string.password_hint, InputType.TYPE_TEXT_VARIATION_PASSWORD)

        signUpButton.setOnClickListener {
            if (usernameEditText.getText().toString() == "") {
                usernameEditText.requestFocus()
                Utils.toastShow(this, "Username field is required")
            } else if (emailEditText.getText().toString() == "") {
                emailEditText.requestFocus()
                Utils.toastShow(this, "Email field is required")
            } else if (passwordEditText.getText().toString() == "") {
                passwordEditText.requestFocus()
                Utils.toastShow(this, "Password field is required")
            } else {
                val username = usernameEditText.getText().toString()
                val email = emailEditText.getText().toString()
                val password = passwordEditText.getText().toString()
                HttpHandler.serviceRegister(this, username, email, password,
                    { res ->
                        //TODO: start a new activity
                        Utils.toastShow(this, "ok")
                    },
                    { err ->
                        //TODO: start a new activity
                        Utils.toastShow(this, "wrong req")
                    })
            }
        }
    }


}