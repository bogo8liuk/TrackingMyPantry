package com.example.trackingmypantry.lib.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReceivedItemsViewModelFactory(val app: Application, val barcode: String, val accessToken: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(Application::class.java, String::class.java, String::class.java)
            .newInstance(app, barcode, accessToken)
    }
}