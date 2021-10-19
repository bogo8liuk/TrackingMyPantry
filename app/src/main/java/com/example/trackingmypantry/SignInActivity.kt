package com.example.trackingmypantry

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingmypantry.lib.EvalMode
import com.example.trackingmypantry.lib.connectivity.net.HttpHandler
import com.example.trackingmypantry.lib.Utils
import com.example.trackingmypantry.lib.ResultCode

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_sign)

        val extras = this.intent.extras
        if (extras != null && extras.getBoolean("retry", false)) {
            Utils.toastShow(this, "A network error occurred," +
                " try to sign in again")
        }

        val usernameEditText = this.findViewById<EditText>(R.id.usernameEditText)
        val emailEditText = this.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = this.findViewById<EditText>(R.id.passwordEditText)
        val signInButton = this.findViewById<AppCompatButton>(R.id.signButton)

        signInButton.setText(R.string.signIn)
        usernameEditText.visibility = android.view.View.GONE

        signInButton.setOnClickListener {
            if (Utils.stringPattern(EvalMode.EMPTY, emailEditText.text.toString())) {
                emailEditText.requestFocus()
                Utils.toastShow(this, "Email field is required")
            } else if (Utils.stringPattern(EvalMode.EMPTY, passwordEditText.text.toString())) {
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
                    { _, _ ->
                        val intent = Intent()
                        this.setResult(ResultCode.NETWORK_ERR, intent)
                        this.finish()
                    }
                )
            }
        }
    }
}