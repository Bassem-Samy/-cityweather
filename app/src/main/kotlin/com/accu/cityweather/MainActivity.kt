package com.accu.cityweather

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.accu.cityweather.forecast.daily.ui.DailyForecastUi
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState.DailyForecast
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState.Loading
import com.accu.cityweather.ui.theme.CityWeatherTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: DailyForecastViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityWeatherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val state = viewModel.viewState.collectAsState(initial = Loading)

                    checkNotificationsPermission(state.value)
                    DailyForecastUi(
                        Modifier.fillMaxSize(),
                        state.value,
                        onDayItemClicked = {
                            viewModel.showDetailDayForecast(it)
                        },
                        onDismissDetailClicked = {
                            viewModel.dismissDetailForecast()
                        },
                        onRetry = {
                            viewModel.onStart(this)
                        }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart(this)
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    private fun checkNotificationsPermission(viewState: ViewState) {
        if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU && viewState is DailyForecast) {
            if (NotificationManagerCompat.from(this).areNotificationsEnabled().not()) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 12
                )
            }
        }
    }
}
