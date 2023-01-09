package com.accu.cityweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.accu.cityweather.forecast.daily.ui.DailyForecastUi
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel
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
                    DailyForecastUi(
                        Modifier.fillMaxSize(),
                        state.value,
                        onDayItemClicked = {
                            viewModel.showDetailDayForecast(it)
                        },
                        onDismissDetailClicked = {
                            viewModel.dismissDetailForecast()
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
