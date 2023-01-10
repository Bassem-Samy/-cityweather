package com.accu.cityweather

import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel
import com.accu.cityweather.forecast.daily.usecase.CityStorage
import com.accu.cityweather.forecast.daily.usecase.GetDailyForecastUseCase
import com.accu.cityweather.forecast.daily.usecase.SharedPreferencesCityStorage
import com.accu.cityweather.forecast.repository.DayDateFormatter
import com.accu.cityweather.forecast.repository.DayDateFormatterImpl
import com.accu.cityweather.forecast.repository.DegreeToCardinalConverter
import com.accu.cityweather.forecast.repository.DegreeToCardinalConverterImpl
import com.accu.cityweather.forecast.repository.ForeCastRepositoryImpl
import com.accu.cityweather.forecast.repository.ForecastRepository
import com.accu.cityweather.forecast.repository.IconUrlResolver
import com.accu.cityweather.forecast.repository.IconUrlResolverImpl
import com.accu.cityweather.location.CityFromLocationProvider
import com.accu.cityweather.location.CityFromLocationUseCase
import com.accu.cityweather.location.GeoCoderCityFromLocationProvider
import com.accu.cityweather.location.LocationProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single<LocationProvider> {
        object : LocationProvider {}
    }
    single<CityFromLocationProvider> {
        GeoCoderCityFromLocationProvider(application = get())
    }

    single<DegreeToCardinalConverter> {
        DegreeToCardinalConverterImpl()
    }

    factory<ForecastRepository> {
        ForeCastRepositoryImpl(
            weatherApi = get(),
            degreeToCardinalConverter = get(),
            dayDateFormatter = get(),
            iconUrlResolver = get()
        )
    }
    single<CityStorage> {
        SharedPreferencesCityStorage(application = get())
    }
    factory {
        CityFromLocationUseCase(cityFromLocationProvider = get())
    }

    factory {
        GetDailyForecastUseCase(
            cityFromLocationUseCase = get(),
            forecastRepository = get(),
            cityStorage = get()
        )
    }

    single<DayDateFormatter> {
        DayDateFormatterImpl()
    }

    single<IconUrlResolver> {
        IconUrlResolverImpl(BuildConfig.WEATHER_ICON_URL)
    }

    viewModel {
        DailyForecastViewModel(
            locationProvider = get(),
            getDailyForecastUseCase = get()
        )
    }
}
