package com.accu.cityweather.forecast.daily.usecase

import android.app.Application
import android.content.Context
import com.accu.cityweather.forecast.repository.DayForecast
import com.accu.cityweather.forecast.repository.ForecastRepository
import com.accu.cityweather.location.CityFromLocationUseCase

class GetDailyForecastUseCase(
    private val cityFromLocationUseCase: CityFromLocationUseCase,
    private val forecastRepository: ForecastRepository,
    private val cityStorage: CityStorage,
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): CityDailyForecast {
        val city = cityFromLocationUseCase(
            longitude = longitude,
            latitude = latitude
        )
        val items = if (city.isNullOrEmpty()) {
            emptyList()
        } else {
            cityStorage.saveCity(city)
            forecastRepository.getDaysForecast(city)
        }
        return CityDailyForecast(city, items)
    }

    data class CityDailyForecast(
        val city: String?,
        val items: List<DayForecast>
    )
}

interface CityStorage {
    fun getCity(): String?
    fun saveCity(city: String)
}

class SharedPreferencesCityStorage(private val application: Application) : CityStorage {

    override fun getCity(): String? {
        return application.getCityPreferences()
            .getString(CITY_KEY, null)
    }

    override fun saveCity(city: String) {
        application.getCityPreferences().edit()
            .putString(CITY_KEY, city).apply()
    }

    private fun Application.getCityPreferences() =
        this.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    companion object {
        const val CITY_KEY = "city_key"
        const val FILE_NAME = "city_file"
    }
}
