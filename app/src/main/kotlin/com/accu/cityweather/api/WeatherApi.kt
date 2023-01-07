package com.accu.cityweather.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/forecast/daily/")
    suspend fun dailyForecast(
        @Query("q") query: String,
        @Query("units") units: String,
        @Query("cnt") count: Int,
    ): ApiDailyForecastResponse

    @GET("data/2.5/forecast/")
    suspend fun currentForecast(
        @Query("q") query: String,
        @Query("units") units: String,
        @Query("cnt") count: Int,
    ): ApiCurrentForecastResponse
}
