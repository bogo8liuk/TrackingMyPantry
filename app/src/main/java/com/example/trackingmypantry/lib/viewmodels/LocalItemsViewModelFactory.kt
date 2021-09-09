package com.example.trackingmypantry.lib.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LocalItemsViewModelFactory(val app: Application, val collection: Long?): ViewModelProvider.Factory {
    override fun <T:ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(Application::class.java, Long::class.java)
            .newInstance(app, collection)
    }
}