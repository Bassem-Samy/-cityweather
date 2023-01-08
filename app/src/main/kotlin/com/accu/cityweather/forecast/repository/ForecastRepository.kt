package com.accu.cityweather.forecast.repository

import java.util.Date

interface ForecastRepository {
    suspend fun getDaysForecast(
        city: String,
        units: String = "metric",
        daysCount: Int = 16
    ): List<DayForecast>
}

data class DayForecast(
    val day: Date,
    val sunrise: Date,
    val sunset: Date,
    val maxTemperature: Int,
    val minTemperature: Int,
    val temperature: DayTemperature,
    val feelsLikeTemperature: DayTemperature,
    val pressure: Int,
    val humidity: Int,
    val wind: Wind?,
    val rain: Rain,
)

data class DayTemperature(
    val day: Int,
    val night: Int,
    val eve: Int,
    val morning: Int
)

data class Rain(val probability: Int, val size: Double)
data class Wind(val speed: Double, val direction: String)
