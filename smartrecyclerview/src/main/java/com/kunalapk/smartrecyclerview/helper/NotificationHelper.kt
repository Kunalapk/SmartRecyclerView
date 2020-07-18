package com.kunalapk.smartrecyclerview.helper

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.kunalapk.smartrecyclerview.R

internal class NotificationHelper(ctx: Context) : ContextWrapper(ctx) {


    private val manager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val chan1 = NotificationChannel(PRIMARY_CHANNEL, getString(R.string.noti_channel_default), NotificationManager.IMPORTANCE_DEFAULT)
            chan1.lightColor = Color.GREEN
            chan1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            manager.createNotificationChannel(chan1)

            val chan2 = NotificationChannel(SECONDARY_CHANNEL, getString(R.string.noti_channel_second), NotificationManager.IMPORTANCE_HIGH)
            chan2.lightColor = Color.BLUE
            chan2.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            manager.createNotificationChannel(chan2)
        }

    }


    fun getNotification1(title: String, body: String,intent: PendingIntent): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, PRIMARY_CHANNEL)
            .setContentTitle(title)
            .setContentIntent(intent)
            .setContentText(body)
            .setSmallIcon(smallIcon)
            .setAutoCancel(true)

    }


    fun getNotification2(title: String, body: String,intent: PendingIntent,icon:Bitmap?): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, SECONDARY_CHANNEL)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(smallIcon)
            .setAutoCancel(true)
            .setContentIntent(intent)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(icon))
    }

    fun getNotification2(title: String, body: String,intent: PendingIntent): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, SECONDARY_CHANNEL)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(smallIcon)
            .setAutoCancel(true)
            .setContentIntent(intent)
    }


    fun notify(id: Int, notification: NotificationCompat.Builder) {
        manager.notify(id, notification.build())
    }


    private val smallIcon: Int
        get() = R.drawable.ic_notification


    companion object {
        val PRIMARY_CHANNEL = "default"
        val SECONDARY_CHANNEL = "second"
    }
}