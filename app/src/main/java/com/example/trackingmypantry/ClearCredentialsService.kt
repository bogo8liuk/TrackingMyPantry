package com.example.trackingmypantry

import android.app.Service
import android.content.Intent
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process
import com.example.trackingmypantry.lib.credentials.CredentialsHandler
import com.example.trackingmypantry.lib.credentials.TokenHandler
import com.example.trackingmypantry.lib.credentials.TokenType

class ClearCredentialsService : Service() {
    override fun onCreate() {
        super.onCreate()

        // Starting the service thread with the lowest priority, it has nothing to do
        HandlerThread("service", Process.THREAD_PRIORITY_LOWEST).apply {
            start()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        /* This is critical for security! Whenever the app gets closed, all the credentials must
        * be canceled, in order not to keep them "inside" the application. This is cannot be
        * performed through lifecycle method onDestroy() of android components like Activity or
        * Application because it's not granted that that method will be called. */
        CredentialsHandler.clearCredentials(this)
        TokenHandler.removeToken(this, TokenType.ACCESS)
        TokenHandler.removeToken(this, TokenType.SESSION)

        this.stopSelf()
    }
}