package com.accu.cityweather.forecast.daily.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.accu.cityweather.forecast.repository.DayForecast

@Composable
fun DailyForecastUi(
    modifier: Modifier = Modifier,
    viewState: ViewState,
    onDayItemClicked: (DayForecast) -> Unit,
    onDismissDetailClicked: () -> Unit,
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
            Error -> TODO()
            Loading -> LoadingUi(Modifier.align(Alignment.Center))
            LocationUnAvailable -> TODO()
            NoResult -> TODO()
        }
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
fun DayDetailForecast(dayForecast: DayForecast) {
    Card(modifier = Modifier.padding(vertical = 16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            DetailHeader(dayForecast)
            DetailInfo(dayForecast)
        }
    }
}

@Composable
fun DetailHeader(dayForecast: DayForecast) {
    with(dayForecast) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(48.dp),
                model = iconUrl,
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                description?.let {
                    Text(
                        text = "${it.main}, ${it.description}",
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = stringResource(
                        id = R.string.day_detail_min_max_temp,
                        maxTemperature,
                        minTemperature
                    )
                )
            }
        }
    }
}

@Composable
fun DetailInfo(dayForecast: DayForecast) {
    with(dayForecast) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(IntrinsicSize.Max),
                color = MaterialTheme.colors.primary
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = stringResource(
                        id = R.string.day_details_condition,
                        rain.size,
                        rain.probability
                    )
                )
                wind?.let {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(
                            id = R.string.day_details_wind,
                            it.speed,
                            it.direction
                        )
                    )
                }
            }
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
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(modifier.padding(16.dp))
        Text(text = stringResource(id = R.string.loading))
    }
}
