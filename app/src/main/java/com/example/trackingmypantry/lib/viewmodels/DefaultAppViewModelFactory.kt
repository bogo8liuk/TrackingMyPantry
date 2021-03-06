package com.example.trackingmypantry.lib.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DefaultAppViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(Application::class.java)
            .newInstance(app)
    }
}