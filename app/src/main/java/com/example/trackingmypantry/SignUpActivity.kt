package com.example.trackingmypantry

import android.content.Intent
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

        var usernameEditText = this.findViewById<EditText>(R.id.usernameEditText)
        var emailEditText = this.findViewById<EditText>(R.id.emailEditText)
        var passwordEditText = this.findViewById<EditText>(R.id.passwordEditText)
        var signUpButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.signButton)

        signUpButton.setText(R.string.sign_up)
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
                        val intent = Intent()
                        this.setResult(RESULT_OK, intent)
                        this.finish()
                    },
                    { statusCode, err ->
                        val intent = Intent()
                        if (statusCode == 500) {
                            this.setResult(Utils.ResultCode.EXISTENT_USER, intent)
                        } else {
                            this.setResult(Utils.ResultCode.NETWORK_ERR, intent)
                        }
                        this.finish()
                    })
            }
        }
    }


}