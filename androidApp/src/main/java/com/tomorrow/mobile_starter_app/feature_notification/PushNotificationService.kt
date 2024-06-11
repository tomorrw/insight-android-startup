package com.tomorrow.mobile_starter_app.feature_notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tomorrow.mobile_starter_app.R
import com.tomorrow.mobile_starter_app.launch.MainActivity
import java.net.URL


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PushNotificationsService : FirebaseMessagingService() {
    private val flag = PendingIntent.FLAG_IMMUTABLE

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val channel = NotificationChannel(
            "general",
            "General",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.setShowBadge(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        try {
            applicationContext
                .getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        } catch (e: Exception) {
            Log.e("NotificationService", "${e.message}")
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = message.data["deeplink"]?.let {
            Intent(
                Intent.ACTION_VIEW,
                it.toUri(),
                applicationContext,
                MainActivity::class.java
            )
        } ?: message.toIntent()


        val localNotificationBuilder = NotificationCompat
            .Builder(applicationContext, "general")
            .setContentTitle(message.notification?.title ?: message.data["title"] ?: "")
            .setContentText(message.notification?.body ?: message.data["body"] ?: "")
            .setSmallIcon(R.drawable.ic_logo_notification)
            .setAutoCancel(true)
            .setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(1, flag)
            })

        (message.notification?.imageUrl ?: message.data["imageUrl"])
            ?.let { URL(it.toString()) }
            ?.let {
                try {
                    BitmapFactory.decodeStream(it.openConnection().getInputStream())
                } catch (e: Throwable) {
                    null
                }
            }?.let {
                localNotificationBuilder.setLargeIcon(it)
            }

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1010, localNotificationBuilder.build())
        }
    }
}