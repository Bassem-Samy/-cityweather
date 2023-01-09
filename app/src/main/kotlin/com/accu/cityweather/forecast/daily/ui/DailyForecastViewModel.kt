package com.accu.cityweather.forecast.daily.ui

import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState.DailyForecast
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState.Error
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState.Loading
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState.LocationUnAvailable
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState.NoResult
import com.accu.cityweather.forecast.daily.usecase.GetDailyForecastUseCase
import com.accu.cityweather.forecast.repository.DayForecast
import com.accu.cityweather.location.LocationProvider
import com.accu.cityweather.location.LocationProvider.LocationResult.FatalError
import com.accu.cityweather.location.LocationProvider.LocationResult.LocationSettingsOff
import com.accu.cityweather.location.LocationProvider.LocationResult.PermissionDenied
import com.accu.cityweather.location.LocationProvider.LocationResult.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DailyForecastViewModel(
    private val locationProvider: LocationProvider,
    private val getDailyForecastUseCase: GetDailyForecastUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow<ViewState>(Loading)
    val viewState: Flow<ViewState> = _viewState
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _viewState.tryEmit(Error)
    }

    fun onStart(context: Context) {
        viewModelScope.launch(coroutineExceptionHandler) {
            when (val locationResult = locationProvider.getCurrentLocation(context)) {
                FatalError -> _viewState.emit(Error)
                LocationSettingsOff,
                PermissionDenied -> _viewState.emit(LocationUnAvailable)
                is Success -> {
                    handleLocation(locationResult.location)
                }
            }
        }
    }

    private suspend fun handleLocation(location: Location) {
        val dailyList = getDailyForecastUseCase(
            longitude = location.longitude,
            latitude = location.latitude
        )
        if (dailyList.isEmpty()) {
            _viewState.emit(NoResult)
        } else {
            _viewState.emit(DailyForecast(dailyList))
        }
    }

    sealed class ViewState {
        object Error : ViewState()
        object LocationUnAvailable : ViewState()
        object Loading : ViewState()
        object NoResult : ViewState()
        data class DailyForecast(val items: List<DayForecast>) : ViewState()
    }
}
