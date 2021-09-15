package com.example.trackingmypantry

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.trackingmypantry.lib.EvalMode
import com.example.trackingmypantry.lib.net.HttpHandler
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.ResultCode

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_sign)

        var usernameEditText = this.findViewById<EditText>(R.id.usernameEditText)
        var emailEditText = this.findViewById<EditText>(R.id.emailEditText)
        var passwordEditText = this.findViewById<EditText>(R.id.passwordEditText)
        var signUpButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.signButton)

        signUpButton.setText(R.string.signUp)
        signUpButton.setOnClickListener {
            if (Utils.stringPattern(EvalMode.EMPTY, usernameEditText.text.toString())) {
                usernameEditText.requestFocus()
                Utils.toastShow(this, "Username field is required")
            } else if (Utils.stringPattern(EvalMode.EMPTY, emailEditText.text.toString())) {
                emailEditText.requestFocus()
                Utils.toastShow(this, "Email field is required")
            } else if (Utils.stringPattern(EvalMode.EMPTY, passwordEditText.text.toString())) {
                passwordEditText.requestFocus()
                Utils.toastShow(this, "Password field is required")
            } else {
                val username = usernameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                HttpHandler.serviceRegister(this, username, email, password,
                    { res ->
                        val intent = Intent()
                        this.setResult(RESULT_OK, intent)
                        this.finish()
                    },
                    { statusCode, _ ->
                        val intent = Intent()
                        if (statusCode == 500) {
                            this.setResult(ResultCode.EXISTENT_USER, intent)
                        } else {
                            this.setResult(ResultCode.NETWORK_ERR, intent)
                        }
                        this.finish()
                    })
            }
        }
    }


}