package com.accu.cityweather

import com.accu.cityweather.location.CityFromLocationProvider
import com.accu.cityweather.location.CityFromLocationUseCase
import com.accu.cityweather.location.GeoCoderCityFromLocationProvider
import com.accu.cityweather.location.LocationProvider
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.dsl.module
import retrofit2.Retrofit

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

@OptIn(ExperimentalSerializationApi::class)
val networkModule = module {
    val contentType = MediaType.get("application/json")
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_API_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }
}
