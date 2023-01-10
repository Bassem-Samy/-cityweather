package com.accu.cityweather.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType.CONNECTED
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await
import com.accu.cityweather.forecast.repository.ForecastRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit.MILLISECONDS

interface ForecastNotificationManager {
    suspend fun schedule(context: Context, repeatInterval: Long)
    suspend fun cancel(context: Context)
}

class WorkManagerForecastNotificationManager(private val notificationHelper: ForecastNotificationHelper) :
    ForecastNotificationManager {

    override suspend fun schedule(context: Context, repeatInterval: Long) {

        // create notification channel
        notificationHelper.createForecastChannel(context)

        // cancel previous work if any
        cancel(context)

        // enqueue new work
        val constraints = Constraints.Builder().setRequiredNetworkType(CONNECTED)
            .setRequiresBatteryNotLow(true).build()

        val workRequest = PeriodicWorkRequestBuilder<CurrentForecastWorker>(
            repeatInterval = repeatInterval,
            repeatIntervalTimeUnit = MILLISECONDS
        )//.setInitialDelay(repeatInterval, MILLISECONDS)
            .setConstraints(constraints)
            .addTag(WORKER_TAG)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    override suspend fun cancel(context: Context) {
        val operation = WorkManager.getInstance(context).cancelAllWorkByTag(WORKER_TAG)
        operation.await()
    }

    companion object {
        private const val WORKER_TAG = "forecast_worker_tag"
    }
}

class CurrentForecastWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams), KoinComponent {

    private val forecastRepository: ForecastRepository by inject()
    private val notificationHelper: ForecastNotificationHelper by inject()
    override suspend fun doWork(): Result {
        notificationHelper.showNotification(appContext)
        return Result.success()
    }
}
