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

        var usernameEditText = this.findViewById<EditText>(R.id.usernameEditText)
        var emailEditText = this.findViewById<EditText>(R.id.emailEditText)
        var passwordEditText = this.findViewById<EditText>(R.id.passwordEditText)
        var signInButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.signButton)

        signInButton.setText(R.string.sign_in)
        usernameEditText.visibility = android.view.View.GONE
    }
}