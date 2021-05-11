package com.example.trackingmypantry

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_sign)

        var insertingArea = findViewById<LinearLayout>(R.id.insertingArea)
        var usernameEditText = EditText(this)
        var emailEditText = EditText(this)
        var passwordEditText = EditText(this)
        var signUpButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.signButton)

        insertingArea.addView(usernameEditText)
        insertingArea.addView(emailEditText)
        insertingArea.addView(passwordEditText)
        signUpButton.setText(R.string.sign_up)
    }


}