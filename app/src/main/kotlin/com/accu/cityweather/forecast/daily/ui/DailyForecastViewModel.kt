package com.accu.cityweather.forecast.daily.ui

import android.content.Context
import android.location.Location
import android.util.Log
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
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("ViewModel", throwable.message ?: "")
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
        val cityDailyForecast = getDailyForecastUseCase(
            longitude = location.longitude,
            latitude = location.latitude
        )
        if (cityDailyForecast.items.isEmpty()) {
            _viewState.emit(NoResult)
        } else {
            _viewState.emit(
                DailyForecast(
                    city = cityDailyForecast.city ?: "",
                    items = cityDailyForecast.items
                )
            )
        }
    }

    fun showDetailDayForecast(dayForecast: DayForecast) {
        (_viewState.value as? DailyForecast)?.let {
            _viewState.tryEmit(it.copy(detailItem = dayForecast))
        }
    }

    fun dismissDetailForecast() {
        (_viewState.value as? DailyForecast)?.let {
            _viewState.tryEmit(it.copy(detailItem = null))
        }
    }

    sealed class ViewState {
        object Error : ViewState()
        object LocationUnAvailable : ViewState()
        object Loading : ViewState()
        object NoResult : ViewState()
        data class DailyForecast(
            val city: String,
            val items: List<DayForecast>,
            val detailItem: DayForecast? = null,
        ) : ViewState()
    }
}
