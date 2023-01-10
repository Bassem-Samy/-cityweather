package com.accu.cityweather.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.accu.cityweather.R
import com.accu.cityweather.forecast.repository.CurrentForecast

interface ForecastNotificationHelper {
    fun createForecastChannel(context: Context)
    suspend fun showNotification(context: Context, dayForecast: CurrentForecast)
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

    override suspend fun showNotification(context: Context, dayForecast: CurrentForecast) {
        with(dayForecast) {
            val bitmap = getIconBitmap(context, icon)
            val builder = NotificationCompat.Builder(context, FORECAST_CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_notification)
                bitmap?.let {
                    setLargeIcon(it)
                }
                setContentTitle(
                    context.getString(
                        R.string.current_forecast_notification_title,
                        title,
                        description
                    )
                )
                    setContentText(
                        context.getString(
                            R.string.current_forecast_notification_content,
                            temperature,
                            feelsLike
                        )
                    )
                if (condition.size > 0) {
                    setStyle(
                        NotificationCompat.BigTextStyle().bigText(
                            context.getString(
                                R.string.current_forecast_notification_large_content,
                                temperature,
                                feelsLike,
                                condition.size,
                                condition.probability
                            )
                        )
                    )
                }
                priority = NotificationCompat.PRIORITY_DEFAULT
            }
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        }
    }

    private suspend fun getIconBitmap(context: Context, icon: String): Bitmap? {
        return try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(icon)
                .allowHardware(false)
                .build()
            val result = (loader.execute(request) as SuccessResult).drawable
            (result as BitmapDrawable).bitmap
        } catch (exception: Exception) {
            Log.e("getIconBitmap", exception.message ?: "No Message")
            null
        }
    }

    companion object {
        const val FORECAST_CHANNEL_ID = "forecast_channel_id"
        const val NOTIFICATION_ID = 1
    }
}
