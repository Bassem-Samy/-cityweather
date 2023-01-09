package com.accu.cityweather.forecast.repository

interface IconUrlResolver {
    fun resolve(icon: String): String
}

class IconUrlResolverImpl(private val baseUrl: String) : IconUrlResolver {
    override fun resolve(icon: String): String {
        return baseUrl.plus("$icon@2x.png")
    }
}
