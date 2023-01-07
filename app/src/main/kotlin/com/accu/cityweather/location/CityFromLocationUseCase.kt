package com.accu.cityweather.location

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

class CityFromLocationUseCase(private val cityFromLocationProvider: CityFromLocationProvider) {
    suspend operator fun invoke(longitude: Double, latitude: Double): String? {
        return cityFromLocationProvider.getCityName(longitude = longitude, latitude = latitude)
    }
}

interface CityFromLocationProvider {
    suspend fun getCityName(
        longitude: Double,
        latitude: Double,
        locale: Locale = Locale.ENGLISH
    ): String?
}

class GeoCoderCityFromLocationProvider(private val application: Application) :
    CityFromLocationProvider {
    override suspend fun getCityName(longitude: Double, latitude: Double, locale: Locale): String? {
        return suspendCancellableCoroutine { continuation ->
            val geoCoder = Geocoder(application, locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geoCoder.getFromLocation(latitude, longitude, 1) {
                    continuation.resume(it.getFirstCity())
                }
            } else {
                @Suppress("DEPRECATION")
                val addresses = geoCoder.getFromLocation(latitude, longitude, 1)
                continuation.resume(addresses.getFirstCity())
            }
        }
    }

    private fun List<Address>?.getFirstCity(): String? = this?.firstOrNull()?.locality
}
