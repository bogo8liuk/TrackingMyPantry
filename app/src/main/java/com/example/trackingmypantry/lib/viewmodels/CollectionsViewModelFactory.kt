package com.example.trackingmypantry.lib.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CollectionsViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(Application::class.java, Long::class.java)
            .newInstance(app)
    }
}