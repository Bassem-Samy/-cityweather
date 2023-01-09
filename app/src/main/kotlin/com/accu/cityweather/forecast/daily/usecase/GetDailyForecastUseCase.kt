package com.accu.cityweather.forecast.daily.usecase

import com.accu.cityweather.forecast.repository.DayForecast
import com.accu.cityweather.forecast.repository.ForecastRepository
import com.accu.cityweather.location.CityFromLocationUseCase

class GetDailyForecastUseCase(
    private val cityFromLocationUseCase: CityFromLocationUseCase,
    private val forecastRepository: ForecastRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): CityDailyForecast {
        val city = cityFromLocationUseCase(
            longitude = longitude,
            latitude = latitude
        )
        val items = if (city.isNullOrEmpty()) {
            emptyList()
        } else {
            // TODO save city
            forecastRepository.getDaysForecast(city)
        }
        return CityDailyForecast(city, items)
    }

    data class CityDailyForecast(
        val city: String?,
        val items: List<DayForecast>
    )
}
