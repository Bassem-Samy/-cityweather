package com.accu.cityweather.forecast.daily.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.accu.cityweather.R
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState.DailyForecast
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState.Error
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState.Loading
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState.LocationUnAvailable
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState.NoResult
import com.accu.cityweather.forecast.repository.Condition
import com.accu.cityweather.forecast.repository.DayDescription
import com.accu.cityweather.forecast.repository.DayForecast
import com.accu.cityweather.forecast.repository.DayTemperature

@Composable
fun DailyForecastUi(
    modifier: Modifier = Modifier,
    viewState: ViewState,
    onDayItemClicked: (DayForecast) -> Unit,
    onDismissDetailClicked: () -> Unit,
    onRetry: () -> Unit,
) {
    Box(modifier = modifier) {
        when (viewState) {
            is DailyForecast -> DaysListUi(
                Modifier.fillMaxSize(),
                viewState.city,
                viewState.items,
                viewState.detailItem,
                onDayItemClicked,
                onDismissDetailClicked,
            )
            Loading -> LoadingUi(Modifier.align(Center))
            LocationUnAvailable -> RetryUi(
                Modifier.align(Center),
                R.string.location_unavailable_retry,
                onRetry
            )
            NoResult -> RetryUi(
                Modifier.align(Center),
                R.string.no_result_retry, onRetry
            )
            Error -> RetryUi(
                Modifier.align(Center), R.string.general_error_retry,
                onRetry
            )
        }
    }
}

@Composable
fun RetryUi(modifier: Modifier, @StringRes message: Int, onRetry: () -> Unit) {
    Button(modifier = modifier, onClick = onRetry) {
        Text(text = stringResource(id = message))
    }
}

@Composable
fun DaysListUi(
    modifier: Modifier,
    cityName: String,
    items: List<DayForecast>,
    detailIDayForecast: DayForecast?,
    onDayItemClicked: (DayForecast) -> Unit,
    onDismissDetailClicked: () -> Unit,
) {
    Column {
        Text(
            text = cityName,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn(modifier = modifier) {
            items(items) { item ->
                DayItem(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(16.dp),
                    dayForecast = item,
                    onclick = onDayItemClicked,
                )
            }
        }
    }
    detailIDayForecast?.let {
        Dialog(onDismissRequest = onDismissDetailClicked) {
            DayDetailForecast(it)
        }
    }
}

@Composable
fun DayItem(
    modifier: Modifier,
    dayForecast: DayForecast,
    onclick: (DayForecast) -> Unit
) {
    Card(modifier = modifier.clickable { onclick(dayForecast) }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = dayForecast.day,
                textAlign = TextAlign.Center
            )

            AsyncImage(
                modifier = Modifier.size(24.dp),
                model = dayForecast.iconUrl,
                contentDescription = null,
            )

            Text(
                text = stringResource(
                    id = R.string.day_min_max_temp,
                    dayForecast.maxTemperature,
                    dayForecast.minTemperature
                )
            )
            if (dayForecast.description != null) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = dayForecast.description.description,
                    textAlign = TextAlign.Center
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun LoadingUi(modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = CenterHorizontally) {
        CircularProgressIndicator(modifier.padding(16.dp))
        Text(text = stringResource(id = R.string.loading))
    }
}

@Preview
@Composable
fun DayItemPreview() {
    DayItem(
        modifier = Modifier,
        dayForecast = DayForecast(
            day = "Sat 08 Jan",
            description = DayDescription("Cloudy", "Cloudy with rain"),
            sunrise = "09:10",
            sunset = "18:00",
            maxTemperature = 10,
            minTemperature = 2,
            temperature = DayTemperature(
                day = 10,
                night = 3,
                eve = 2,
                morning = 5
            ),
            feelsLikeTemperature = DayTemperature(
                day = 0,
                night = 0,
                eve = 0,
                morning = 0
            ),
            pressure = 0,
            humidity = 0,
            wind = null,
            condition = Condition(
                probability = 0,
                size = 0.0
            ),
            iconUrl = ""
        ),
        onclick = {}
    )
}
