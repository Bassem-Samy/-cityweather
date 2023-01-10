package com.accu.cityweather.forecast.daily.ui

import android.content.Context
import android.location.Location
import app.cash.turbine.test
import com.accu.cityweather.CoroutineTestRule
import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel.ViewState
import com.accu.cityweather.forecast.daily.usecase.GetDailyForecastUseCase
import com.accu.cityweather.forecast.daily.usecase.GetDailyForecastUseCase.CityDailyForecast
import com.accu.cityweather.forecast.repository.Condition
import com.accu.cityweather.forecast.repository.DayDescription
import com.accu.cityweather.forecast.repository.DayForecast
import com.accu.cityweather.forecast.repository.DayTemperature
import com.accu.cityweather.forecast.repository.Wind
import com.accu.cityweather.location.LocationProvider
import com.accu.cityweather.location.LocationProvider.LocationResult
import com.accu.cityweather.notification.ForecastNotificationManager
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DailyForecastViewModelTest {
    @get:Rule
    val testRule: CoroutineTestRule = CoroutineTestRule()
    private val locationProvider = mockk<LocationProvider>()
    private val getDailyForecastUseCase = mockk<GetDailyForecastUseCase>(relaxed = true)
    private val forecastNotificationManager = mockk<ForecastNotificationManager> {
        coEvery { cancel(any()) } just runs
        coEvery { schedule(any(), any()) } just runs
    }
    private val context = mockk<Context>()
    private val lat = 52.500459
    private val long = 13.495987
    private val mockLocation = mockk<Location>() {
        every { latitude } returns lat
        every { longitude } returns long
    }
    private lateinit var viewModel: DailyForecastViewModel

    @Test
    fun `initial state is loading`() = runTest {
        init()

        viewModel.viewState.test {
            val state = awaitItem()
            assertThat(state).isEqualTo(ViewState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `if location result returns error state is error`() {
        coEvery { locationProvider.getCurrentLocation(context) } returns LocationResult.FatalError
        init()
        runTest {
            viewModel.viewState.test {
                awaitItem()
                viewModel.onStart(context)
                val state = awaitItem()
                assertThat(state).isEqualTo(ViewState.Error)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `if location permission denied returns location unavailable`() {
        coEvery { locationProvider.getCurrentLocation(context) } returns LocationResult.PermissionDenied
        init()
        runTest {
            viewModel.viewState.test {
                awaitItem()
                viewModel.onStart(context)
                val state = awaitItem()
                assertThat(state).isEqualTo(ViewState.LocationUnAvailable)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when Location Success available gets daily forecast by location`() {

        coEvery { locationProvider.getCurrentLocation(context) } returns LocationResult.Success(
            mockLocation
        )
        init()
        runTest {

            viewModel.onStart(context)
            advanceUntilIdle()
            coVerify(exactly = 1) { getDailyForecastUseCase(latitude = lat, longitude = long) }
        }
    }

    @Test
    fun `when gets daily items viewState is DailyForecast `() {

        coEvery { locationProvider.getCurrentLocation(context) } returns LocationResult.Success(
            mockLocation
        )
        coEvery {
            getDailyForecastUseCase(
                latitude = lat,
                longitude = long
            )
        } returns cityDailyForecast
        init()
        runTest {
            viewModel.viewState.test {
                awaitItem()
                viewModel.onStart(context)
                val actual = awaitItem()
                assertThat(actual).isEqualTo(
                    ViewState.DailyForecast(
                        city = "Berlin",
                        items = cityDailyForecast.items,
                        detailItem = null
                    )
                )
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when item is clicked detail item is filled`() {

        coEvery { locationProvider.getCurrentLocation(context) } returns LocationResult.Success(
            mockLocation
        )
        coEvery {
            getDailyForecastUseCase(
                latitude = lat,
                longitude = long
            )
        } returns cityDailyForecast
        init()
        runTest {
            viewModel.viewState.test {
                awaitItem()
                viewModel.onStart(context)
                awaitItem()
                viewModel.showDetailDayForecast(cityDailyForecast.items.last())
                val actual = awaitItem()
                assertThat(actual).isEqualTo(
                    ViewState.DailyForecast(
                        "Berlin",
                        cityDailyForecast.items,
                        cityDailyForecast.items.last()
                    )
                )
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when item dimiss detail requested detail item is null`() {

        coEvery { locationProvider.getCurrentLocation(context) } returns LocationResult.Success(
            mockLocation
        )
        coEvery {
            getDailyForecastUseCase(
                latitude = lat,
                longitude = long
            )
        } returns cityDailyForecast
        init()
        runTest {
            viewModel.viewState.test {
                awaitItem()
                viewModel.onStart(context)
                awaitItem()
                viewModel.showDetailDayForecast(cityDailyForecast.items.last())
                val state = awaitItem()
                assertThat(state).isEqualTo(
                    ViewState.DailyForecast(
                        "Berlin",
                        cityDailyForecast.items,
                        cityDailyForecast.items.last()
                    )
                )
                viewModel.dismissDetailForecast()
                val actual = awaitItem()
                assertThat(actual).isEqualTo(
                    ViewState.DailyForecast(
                        "Berlin",
                        cityDailyForecast.items,
                        null
                    )
                )
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `schedule notification onStop`() = runTest {
        init()
        viewModel.onStop(context)
        advanceUntilIdle()
        coVerify(exactly = 1) { forecastNotificationManager.schedule(context, 15 * 60 * 1000) }
    }

    @Test
    fun `cancels notification onStart`() = runTest {
        coEvery { locationProvider.getCurrentLocation(context) } returns LocationResult.Success(
            mockLocation
        )
        coEvery {
            getDailyForecastUseCase(
                latitude = lat,
                longitude = long
            )
        } returns cityDailyForecast
        init()
        viewModel.onStart(context)
        advanceUntilIdle()
        coVerify(exactly = 1) { forecastNotificationManager.cancel(context) }
    }

    private fun init() {
        viewModel = DailyForecastViewModel(
            locationProvider = locationProvider,
            getDailyForecastUseCase = getDailyForecastUseCase,
            forecastNotificationManager = forecastNotificationManager
        )
    }

    private val cityDailyForecast = CityDailyForecast(
        city = "Berlin",
        items = listOf(
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
    )
}
