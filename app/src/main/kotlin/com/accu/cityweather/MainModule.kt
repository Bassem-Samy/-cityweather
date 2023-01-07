package com.accu.cityweather

import com.accu.cityweather.location.CityFromLocationProvider
import com.accu.cityweather.location.CityFromLocationUseCase
import com.accu.cityweather.location.GeoCoderCityFromLocationProvider
import com.accu.cityweather.location.LocationProvider
import org.koin.dsl.module

val mainModule = module {
    single<LocationProvider> {
        object : LocationProvider {}
    }
    single<CityFromLocationProvider> {
        GeoCoderCityFromLocationProvider(application = get())
    }
    factory {
        CityFromLocationUseCase(cityFromLocationProvider = get())
    }
}
