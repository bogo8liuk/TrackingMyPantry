package com.example.trackingmypantry

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

open class SecureChangesActivity : AppCompatActivity() {
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Unsaved changes")
            .setMessage("There are unsaved changes, by going back you will" +
                    "lose them. Continuing?")
            .setNegativeButton(R.string.negative, null)
            .setPositiveButton(R.string.positive, DialogInterface.OnClickListener { _, _ ->
                super.onBackPressed()
            })
    }
}