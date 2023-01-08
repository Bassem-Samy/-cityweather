package com.accu.cityweather.forecast.daily.usecase

import com.accu.cityweather.forecast.repository.DayForecast
import com.accu.cityweather.forecast.repository.ForecastRepository
import com.accu.cityweather.location.CityFromLocationUseCase

class GetDailyForecastUseCase(
    private val cityFromLocationUseCase: CityFromLocationUseCase,
    private val forecastRepository: ForecastRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): List<DayForecast> {
        val city = cityFromLocationUseCase(
            longitude = longitude,
            latitude = latitude
        )
        return if (city.isNullOrEmpty()) {
            emptyList()
        } else {
            // TODO save city
            forecastRepository.getDaysForecast(city)
        }
    }
}
