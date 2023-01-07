package com.accu.cityweather

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CityWeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CityWeatherApplication)
            modules(
                listOf(
                    mainModule,
                    networkModule
                )
            )
        }
    }
}
