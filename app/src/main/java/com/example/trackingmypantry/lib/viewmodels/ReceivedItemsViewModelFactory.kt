package com.example.trackingmypantry.lib.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReceivedItemsViewModelFactory(val app: Application, val barcode: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(Application::class.java, String::class.java)
            .newInstance(app, barcode)
    }
}