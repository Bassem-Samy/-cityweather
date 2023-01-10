package com.accu.cityweather.forecast.repository

import com.accu.cityweather.forecast.repository.GeneralTemperature.COLD
import com.accu.cityweather.forecast.repository.GeneralTemperature.FREEZING
import com.accu.cityweather.forecast.repository.GeneralTemperature.HOT
import com.accu.cityweather.forecast.repository.GeneralTemperature.VERY_COLD

interface ForecastRepository {
    suspend fun getDaysForecast(
        city: String,
        units: String = "metric",
        daysCount: Int = 16
    ): List<DayForecast>

    suspend fun getCurrentForecast(
        city: String,
        units: String = "metric",
        count: Int = 1,
    ): CurrentForecast
}

data class CurrentForecast(
    val temperature: Int = 0,
    val feelsLike: Int = 0,
    val title: String = "-",
    val description: String = "-",
    val icon: String = "-",
    val condition: Condition = Condition(0, 0.0),
    val isRain: Boolean = false,
)

data class DayForecast(
    val day: String,
    val description: DayDescription?,
    val sunrise: String,
    val sunset: String,
    val maxTemperature: Int,
    val minTemperature: Int,
    val temperature: DayTemperature,
    val feelsLikeTemperature: DayTemperature,
    val pressure: Int,
    val humidity: Int,
    val wind: Wind?,
    val condition: Condition,
    val iconUrl: String,
)

fun DayForecast.generalTemperature(): GeneralTemperature =
    when {
        maxTemperature > 24 -> HOT
        maxTemperature in 11..24 -> COLD
        maxTemperature in 1..10 -> VERY_COLD
        else -> FREEZING
    }

enum class GeneralTemperature {
    HOT,
    COLD,
    VERY_COLD,
    FREEZING,
}

data class DayDescription(val main: String, val description: String)
data class DayTemperature(
    val day: Int,
    val night: Int,
    val eve: Int,
    val morning: Int
)

data class Condition(val probability: Int, val size: Double)
data class Wind(val speed: Double, val direction: String)
