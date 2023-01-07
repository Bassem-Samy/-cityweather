package com.accu.cityweather.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiCurrentForecastResponse(val list: List<ApiCurrentForecast>?)

@Serializable
data class ApiDailyForecastResponse(val list: List<ApiDailyForecast>)

@Serializable
data class ApiDailyForecast(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: ApiTemp,
    val feels_like: ApiFeelsLike,
    val pressure: Int,
    val humidity: Int,
    val weather: List<ApiWeather>,
    val speed: Double?,
    val deg: Double?,
    val clouds: Double,
    val pop: Double,
    val rain: Double? = null
)

@Serializable
data class ApiTemp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

@Serializable
data class ApiFeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double,
)

@Serializable
data class ApiCurrentForecast(
    val dt: Long,
    val main: ApiMainInfo,
    val weather: List<ApiWeather>,
    val clouds: ApiClouds,
    val wind: ApiWind,
    val visibility: Long? = null,
    val pop: Double?,
    val sys: ApiSys,
    val rain: ApiRain? = null,
)

@Serializable
data class ApiWeather(
    val main: String?,
    val description: String?,
    val icon: String?
)


@Serializable
data class ApiMainInfo(
    val temp: Double?,
    val feels_like: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
    val pressure: Int?,
    val sea_level: Int?,
    val grnd_level: Int?,
    val humidity: Int?,
    val temp_kf: Double?
)

@Serializable
data class ApiClouds(val all: Int?)

@Serializable
data class ApiWind(val speed: Double?, val deg: Double?, val gust: Double?)

@Serializable
data class ApiRain(@SerialName("3h") val h: Double?)

@Serializable
data class ApiSys(val pod: String?)

