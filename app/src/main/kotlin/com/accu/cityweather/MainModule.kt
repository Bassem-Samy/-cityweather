package com.accu.cityweather

import com.accu.cityweather.api.WeatherApi
import com.accu.cityweather.forecast.repository.DegreeToCardinalConverter
import com.accu.cityweather.forecast.repository.DegreeToCardinalConverterImpl
import com.accu.cityweather.forecast.repository.ForeCastRepositoryImpl
import com.accu.cityweather.forecast.repository.ForecastRepository
import com.accu.cityweather.location.CityFromLocationProvider
import com.accu.cityweather.location.CityFromLocationUseCase
import com.accu.cityweather.location.GeoCoderCityFromLocationProvider
import com.accu.cityweather.location.LocationProvider
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
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
    single<DegreeToCardinalConverter> {
        DegreeToCardinalConverterImpl()
    }

    factory<ForecastRepository> {
        ForeCastRepositoryImpl(
            weatherApi = get(), degreeToCardinalConverter = get()
        )
    }
}


@OptIn(ExperimentalSerializationApi::class)
val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single<Retrofit> {
        Retrofit.Builder()
            .client(createOkHttpClient())
            .baseUrl(BuildConfig.WEATHER_API_URL)
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            ).build()
    }

    single<WeatherApi> {
        get<Retrofit>().create(WeatherApi::class.java)
    }
}

private fun createOkHttpClient(key: String = BuildConfig.WEATHER_API_APP_ID): OkHttpClient {
    return OkHttpClient().newBuilder()
        .addInterceptor(ApiKeyInterceptor(key))
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(BODY) })
        .build()
}

class ApiKeyInterceptor(private val key: String) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val originalRequest = chain.request()
        val urlBuilder = originalRequest.url.newBuilder()
        urlBuilder.addQueryParameter("appid", key)
        val newRequest = originalRequest.newBuilder().url(urlBuilder.build()).build()
        return chain.proceed(newRequest)
    }

}
