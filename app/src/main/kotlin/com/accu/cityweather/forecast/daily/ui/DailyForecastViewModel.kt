package com.accu.cityweather.forecast.daily.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accu.cityweather.forecast.daily.usecase.GetDailyForecastUseCase
import com.accu.cityweather.location.LocationProvider
import com.accu.cityweather.location.LocationProvider.LocationResult.FatalError
import com.accu.cityweather.location.LocationProvider.LocationResult.LocationSettingsOff
import com.accu.cityweather.location.LocationProvider.LocationResult.PermissionDenied
import com.accu.cityweather.location.LocationProvider.LocationResult.Success
import kotlinx.coroutines.launch

class DailyForecastViewModel(
    private val locationProvider: LocationProvider,
    private val getDailyForecastUseCase: GetDailyForecastUseCase,
) : ViewModel() {
    fun onStart(context: Context) {
        viewModelScope.launch {
            when (val locationResult = locationProvider.getCurrentLocation(context)) {
                FatalError -> TODO()
                LocationSettingsOff -> TODO()
                PermissionDenied -> TODO()
                is Success -> {
                    val dailyList = getDailyForecastUseCase(
                        longitude = locationResult.location.longitude,
                        latitude = locationResult.location.latitude
                    )
                    Log.d("Result", dailyList.toString())
                }
            }
        }
    }
}
