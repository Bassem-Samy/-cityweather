package com.accu.cityweather.api

import com.accu.cityweather.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder

@OptIn(ExperimentalSerializationApi::class)
val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single<Retrofit> {
        Builder()
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
