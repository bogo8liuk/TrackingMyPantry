package com.example.trackingmypantry

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.trackingmypantry.lib.Utils

class SignInActivity : AppCompatActivity() {
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

        Utils.setEditText(usernameEditText, R.string.username_hint, InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
        Utils.setEditText(passwordEditText, R.string.password_hint, InputType.TYPE_TEXT_VARIATION_PASSWORD)
    }
}