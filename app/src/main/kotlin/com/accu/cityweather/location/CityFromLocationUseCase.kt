package com.accu.cityweather.location

class CityFromLocationUseCase(private val cityFromLocationProvider: CityFromLocationProvider) {
    suspend operator fun invoke(longitude: Double, latitude: Double): String? {
        return cityFromLocationProvider.getCityName(longitude = longitude, latitude = latitude)
    }
}
