package com.example.trackingmypantry

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.trackingmypantry.lib.net.HttpHandler
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.net.ResultCode

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_sign)

        var usernameEditText = this.findViewById<EditText>(R.id.usernameEditText)
        var emailEditText = this.findViewById<EditText>(R.id.emailEditText)
        var passwordEditText = this.findViewById<EditText>(R.id.passwordEditText)
        var signInButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.signButton)

        signInButton.setText(R.string.signIn)
        usernameEditText.visibility = android.view.View.GONE

        signInButton.setOnClickListener {
            if (emailEditText.text.toString() == "") {
                emailEditText.requestFocus()
                Utils.toastShow(this, "Email field is required")
            } else if (passwordEditText.text.toString() == "") {
                passwordEditText.requestFocus()
                Utils.toastShow(this, "Password field is required")
            } else {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                HttpHandler.serviceAuthenticate(this, email, password,
                    { res ->
                        val intent = Intent()
                        this.setResult(RESULT_OK, intent)
                        this.finish()
                    },
                    { statusCode, _ ->
                        val intent = Intent()
                        if (statusCode == 401) {
                            this.setResult(ResultCode.EXPIRED_TOKEN, intent)
                        } else {
                            this.setResult(ResultCode.NETWORK_ERR, intent)
                        }
                        this.finish()
                    }
                )
            }
        }
    }
}