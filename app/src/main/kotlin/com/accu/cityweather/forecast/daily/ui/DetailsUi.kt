package com.accu.cityweather.forecast.daily.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.accu.cityweather.R.string
import com.accu.cityweather.forecast.repository.Condition
import com.accu.cityweather.forecast.repository.DayDescription
import com.accu.cityweather.forecast.repository.DayForecast
import com.accu.cityweather.forecast.repository.DayTemperature
import com.accu.cityweather.forecast.repository.Wind

@Composable
fun DayDetailForecast(dayForecast: DayForecast) {
    Card(modifier = Modifier.padding(vertical = 16.dp)) {
        Column(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 16.dp),
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                text = dayForecast.day, style = MaterialTheme.typography.h6
            )
            DetailHeader(modifier = Modifier.align(Alignment.CenterHorizontally), dayForecast)
            DetailInfo(dayForecast)
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                Modifier
                    .size(width = 64.dp, height = 1.dp)
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            DetailTemperatures(dayForecast)
        }
    }
}

@Composable
fun DetailHeader(modifier: Modifier, dayForecast: DayForecast) {
    with(dayForecast) {
        Row(
            modifier = modifier.padding(vertical = 16.dp),
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
                Row {
                    description?.let {
                        Text(
                            text = "${it.main}, ${it.description}",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (condition.size > 0) {
                        Text(
                            modifier = modifier.padding(start = 4.dp),
                            text = stringResource(
                                id = string.day_detail_condition_size,
                                condition.size
                            )
                        )
                    }
                }
                Text(
                    text = stringResource(
                        id = string.day_detail_min_max_temp,
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
        Row {
            Text(
                text = stringResource(
                    id = string.day_details_condition,
                    condition.probability
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            wind?.let {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(
                        id = string.day_details_wind,
                        it.speed,
                        it.direction
                    )
                )
            }
        }
        Row {
            Text(
                text = stringResource(
                    id = string.day_details_humidity,
                    humidity
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(
                    id = string.day_details_pressure,
                    pressure
                )
            )
        }
        Row {
            Text(
                text = stringResource(
                    id = string.day_details_sunrise,
                    sunrise
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(
                    id = string.day_details_sunset,
                    sunset
                )
            )
        }
    }
}

@Composable
fun DetailTemperatures(dayForecast: DayForecast) {
    with(dayForecast) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                Text(text = "")
                Text(text = stringResource(id = string.morning))
                Text(text = stringResource(id = string.afternoon))
                Text(text = stringResource(id = string.evening))
                Text(text = stringResource(id = string.night))
            }
            with(temperature) {
                DayTimeTemperature(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = string.temperature),
                    morning = morning,
                    afternoon = day,
                    evening = eve,
                    night = night
                )
            }
            with(feelsLikeTemperature) {
                DayTimeTemperature(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = string.day_details_real_feel),
                    morning = morning,
                    afternoon = day,
                    evening = eve,
                    night = night
                )
            }
        }
    }
}

@Composable
fun DayTimeTemperature(
    modifier: Modifier,
    title: String,
    morning: Int,
    afternoon: Int,
    evening: Int,
    night: Int
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title)
        Text(text = stringResource(id = string.temperature_with_degree, morning))
        Text(text = stringResource(id = string.temperature_with_degree, afternoon))
        Text(text = stringResource(id = string.temperature_with_degree, evening))
        Text(text = stringResource(id = string.temperature_with_degree, night))
    }
}

@Preview
@Composable
private fun PreviewDayDetailForecast() {
    DayDetailForecast(
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
                day = 8,
                night = 1,
                eve = 2,
                morning = 1
            ),
            pressure = 100,
            humidity = 30,
            wind = Wind(speed = 4.0, direction = "SW"),
            condition = Condition(
                probability = 30,
                size = 2.0
            ),
            iconUrl = ""
        )
    )
}
