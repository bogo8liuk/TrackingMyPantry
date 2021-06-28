package com.example.trackingmypantry.lib

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trackingmypantry.db.entities.Item

class ItemsViewModel: ViewModel() {
    private val items: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>().also {

        }
    }

    fun getItems(): LiveData<List<Item>> {
        return items
    }
}