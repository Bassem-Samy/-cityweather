package com.accu.cityweather

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.accu.cityweather.forecast.repository.ForecastRepository
import com.accu.cityweather.location.CityFromLocationUseCase
import com.accu.cityweather.location.LocationProvider
import com.accu.cityweather.ui.theme.CityWeatherTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val cityFromLocationUseCase: CityFromLocationUseCase by inject()
    private val forecastRepository: ForecastRepository by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityWeatherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
        val locationProvider = object : LocationProvider {}
        lifecycleScope.launchWhenResumed {
            val result = locationProvider.getCurrentLocation(this@MainActivity)
            when (result) {
                LocationProvider.LocationResult.FatalError,
                LocationProvider.LocationResult.LocationSettingsOff,
                LocationProvider.LocationResult.PermissionDenied -> Log.d(
                    "Location Failed",
                    result.toString()
                )
                is LocationProvider.LocationResult.Success -> {
                    val city = cityFromLocationUseCase(
                        latitude = result.location.latitude,
                        longitude = result.location.longitude
                    )
                    Log.d("City", city ?: "NoCity")
                    city?.let {

                        val result = forecastRepository.getDaysForecast(city = it)
                        Log.d("Result", result.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CityWeatherTheme {
        Greeting("Android")
    }
}
