package com.example.trackingmypantry.lib.connectivity.net

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.Volley

class ReqQueueSingleton(context: Context) {
    companion object {
        @Volatile   // `writes` immediately made visible to other threads
        private var queueInstance: ReqQueueSingleton? = null

        fun getInstance(context: Context): ReqQueueSingleton {
            // if the instance is not instantiated, then do it, else a request queue already exists.
            // synchronized() is used to execute code, blocking concurrent accesses to the object
            return queueInstance ?: synchronized(this) {
                queueInstance ?: ReqQueueSingleton(context).also {
                    queueInstance = it
                }
            }
        }
    }

    private val requestQueue = Volley.newRequestQueue(context.applicationContext)

    fun <T> addRequest(req: Request<T>) {
        requestQueue.add(req)
    }
}