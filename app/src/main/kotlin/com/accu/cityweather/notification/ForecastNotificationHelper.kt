package com.accu.cityweather.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.accu.cityweather.R

interface ForecastNotificationHelper {
    fun createForecastChannel(context: Context)
    fun showNotification(context: Context)
}

class ForeCastNotificationHelperImpl : ForecastNotificationHelper {
    override fun createForecastChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.forecast_notification_channel_name)
            val descriptionText =
                context.getString(R.string.forecast_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(FORECAST_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun showNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, FORECAST_CHANNEL_ID)
            .setSmallIcon(org.koin.android.R.drawable.abc_ab_share_pack_mtrl_alpha)
            .setContentTitle("title")
            .setContentText("content")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        const val FORECAST_CHANNEL_ID = "forecast_channel_id"
        const val NOTIFICATION_ID = 1
    }
}