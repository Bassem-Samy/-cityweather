package com.accu.cityweather.forecast.repository

import kotlin.math.floor

interface DegreeToCardinalConverter {
    fun convert(degree: Double): String
}

class DegreeToCardinalConverterImpl : DegreeToCardinalConverter {
    private val directions = listOf(
        "N",
        "NNE",
        "NE",
        "ENE",
        "E",
        "ESE",
        "SE",
        "SSE",
        "S",
        "SSW",
        "SW",
        "WSW",
        "W",
        "WNW",
        "NW",
        "NNW"
    )

    override fun convert(degree: Double): String {
        return directions[floor(((degree + 11.25) % 360) / 22.5).toInt()]
    }
}
