package com.example.trackingmypantry.lib.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LocalItemsViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T:ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(Application::class.java)
            .newInstance(app)
    }
}