package com.accu.cityweather

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel
import com.accu.cityweather.forecast.daily.ui.DaysListUi
import com.accu.cityweather.forecast.repository.Condition
import com.accu.cityweather.forecast.repository.DayDescription
import com.accu.cityweather.forecast.repository.DayForecast
import com.accu.cityweather.forecast.repository.DayTemperature
import com.accu.cityweather.forecast.repository.Wind
import com.accu.cityweather.ui.theme.CityWeatherTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DayListItemUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    val viewModel: DailyForecastViewModel = mockk(relaxed = true)

    @Test
    fun testCityNameIsDisplayed() {
        composeTestRule.setContent {
            CityWeatherTheme() {
                DaysListUi(
                    modifier = Modifier,
                    cityName = "Berlin",
                    items = listOf(),
                    detailIDayForecast = null,
                    onDayItemClicked = {},
                    onDismissDetailClicked = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Berlin").assertIsDisplayed()
    }

    @Test
    fun dayListIsDisplayed() {
        composeTestRule.setContent {
            CityWeatherTheme() {
                DaysListUi(
                    modifier = Modifier,
                    cityName = "Berlin",
                    items = days,
                    detailIDayForecast = null,
                    onDayItemClicked = {}
                ) {}
            }
        }
        // date displayed
        composeTestRule.onNodeWithText("Sun, 12 Jan").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mon, 13 Jan").assertIsDisplayed()

        // description is displayed
        composeTestRule.onNodeWithText("Cloudy with some rain").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cloudy with a chance of meatballs")
            .assertIsDisplayed()

        // min max temperature is displayed

        composeTestRule.onNodeWithText(
            getContext().getString(R.string.day_min_max_temp, "8", "2")
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            getContext().getString(R.string.day_min_max_temp, "5", "1")
        ).assertIsDisplayed()
    }

    @Test
    fun onDayItemClickCallsViewModel() {
        composeTestRule.setContent {
            CityWeatherTheme() {
                DaysListUi(
                    modifier = Modifier,
                    cityName = "Berlin",
                    items = days,
                    detailIDayForecast = null,
                    onDayItemClicked = { viewModel.showDetailDayForecast(it) }
                ) {}
            }
        }
        composeTestRule.onNodeWithText("Mon, 13 Jan").performClick()
        verify(exactly = 1) { viewModel.showDetailDayForecast(days.last()) }
    }

    private fun getContext() = getInstrumentation().targetContext
    private val days = listOf(
        DayForecast(
            day = "Sun, 12 Jan",
            description = DayDescription("Cloudy", "Cloudy with some rain"),
            sunrise = "08:00",
            sunset = "17:50",
            maxTemperature = 8,
            minTemperature = 2,
            temperature = DayTemperature(
                day = 8,
                night = 2,
                eve = 5,
                morning = 3
            ),
            feelsLikeTemperature = DayTemperature(
                day = 6,
                night = 1,
                eve = 2,
                morning = 2
            ),
            pressure = 130,
            humidity = 30,
            wind = Wind(30.0, "SE"),
            condition = Condition(
                probability = 30,
                size = 01.0
            ),
            iconUrl = "iconUrl"
        ),
        DayForecast(
            day = "Mon, 13 Jan",
            description = DayDescription("Cloudy", "Cloudy with a chance of meatballs"),
            sunrise = "07:50",
            sunset = "17:55",
            maxTemperature = 5,
            minTemperature = 1,
            temperature = DayTemperature(
                day = 4,
                night = 1,
                eve = 2,
                morning = 1
            ),
            feelsLikeTemperature = DayTemperature(
                day = 3,
                night = 1,
                eve = 2,
                morning = 1
            ),
            pressure = 45,
            humidity = 10,
            wind = Wind(10.0, "NW"),
            condition = Condition(
                probability = 10,
                size = 02.0
            ),
            iconUrl = "iconUrl2"
        )
    )
}
