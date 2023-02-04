package com.example.floatingwindowapp.service

import IFLService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.floatingwindowapp.AppContext
import com.example.floatingwindowapp.R
import com.example.floatingwindowapp.ui.main.Window

private const val TAG = "FLService"

class FLService : Service() {
    private val stub = ServiceStub()

    override fun onCreate() {
        super.onCreate()
        AppContext.set(application)
    }

    override fun onBind(p0: Intent?): IBinder = stub

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        showNotification()

        return START_STICKY
    }

    private fun showNotification() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // From Android O, it's necessary to create a notification channel first.
        try {
            with(
                NotificationChannel(
                    "general",
                    "general",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            ) {
                enableLights(false)
                setShowBadge(false)
                enableVibration(false)
                setSound(null, null)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                manager.createNotificationChannel(this)
            }
        } catch (ignored: Exception) {
            // Ignore exception.
        }

        with(
            NotificationCompat.Builder(
                this,
                "general"
            )
        ) {
            setTicker(null)
            setContentTitle(getString(R.string.app_name))
            setContentText(getString(R.string.notification_text))
            setAutoCancel(false)
            setOngoing(true)
            setWhen(System.currentTimeMillis())
            priority = Notification.PRIORITY_DEFAULT
            addAction(
                NotificationCompat.Action(
                    0,
                    getString(R.string.notification_exit),
                    null
                )
            )
            startForeground(1, build())
        }
    }

    inner class ServiceStub : IFLService.Stub() {
        override fun showWindow(windowToken: IBinder?) {
            Handler(Looper.getMainLooper()).post {
                assert(windowToken != null)
                Log.d(TAG, "showing window token: $windowToken")
                Window.show(windowToken)
            }
        }

        override fun moveOffset(windowToken: IBinder?, offset: Float) {
            Handler(Looper.getMainLooper()).post {
                Window.move(offset)
            }
        }

        override fun removeWindow() {
            Handler(Looper.getMainLooper()).post {
                Window.hide()
            }
        }

        override fun enableTouch() {
            Handler(Looper.getMainLooper()).post {
                Window.setTouch(true)
            }
        }

    }

}
