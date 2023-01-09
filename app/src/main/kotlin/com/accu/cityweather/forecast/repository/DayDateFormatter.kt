package com.accu.cityweather.forecast.repository

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface DayDateFormatter {
    fun format(date: Long): String
}

class DayDateFormatterImpl : DayDateFormatter {
    private val pattern = "E, MMM yy"
    private val simpleDateFormatter = SimpleDateFormat(pattern, Locale.US)
    override fun format(date: Long): String =
        simpleDateFormatter.format(Date(date.toMilliSeconds()))

    private fun Long.toMilliSeconds() = this * 1000L
}
