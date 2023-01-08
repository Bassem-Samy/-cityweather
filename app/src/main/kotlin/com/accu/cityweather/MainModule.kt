package com.accu.cityweather

import com.accu.cityweather.forecast.daily.ui.DailyForecastViewModel
import com.accu.cityweather.forecast.daily.usecase.GetDailyForecastUseCase
import com.accu.cityweather.forecast.repository.DegreeToCardinalConverter
import com.accu.cityweather.forecast.repository.DegreeToCardinalConverterImpl
import com.accu.cityweather.forecast.repository.ForeCastRepositoryImpl
import com.accu.cityweather.forecast.repository.ForecastRepository
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
            weatherApi = get(), degreeToCardinalConverter = get()
        )
    }

    factory {
        CityFromLocationUseCase(cityFromLocationProvider = get())
    }

    factory {
        GetDailyForecastUseCase(
            cityFromLocationUseCase = get(),
            forecastRepository = get()
        )
    }

    viewModel {
        DailyForecastViewModel(
            locationProvider = get(),
            getDailyForecastUseCase = get()
        )
    }
}
