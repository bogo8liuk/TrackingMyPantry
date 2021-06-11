package com.example.trackingmypantry.lib

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.Volley

class ReqQueueSingleton(context: Context) {
    companion object {
        @Volatile
        private var queueInstance: ReqQueueSingleton? = null
        fun getInstance(context: Context) =
            // if the instance is not instatiated, then do it, else it already exists a request queue
            // synchronized() is used to block concurrent accesses to the object
            queueInstance ?: synchronized(this) {
                queueInstance ?: ReqQueueSingleton(context).also {
                    queueInstance = it
                }
            }
    }

    private val requestQueue = Volley.newRequestQueue(context.applicationContext)

    fun <T> addRequest(req: Request<T>) {
        requestQueue.add(req)
    }
}