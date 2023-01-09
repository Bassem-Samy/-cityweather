package com.accu.cityweather.forecast.repository

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface DayDateFormatter {
    fun format(date: Long): String
    fun formatTime(date: Long): String
}

class DayDateFormatterImpl : DayDateFormatter {
    private val dayPattern = "E, MMM dd"
    private val timePattern = "HH:mm"
    private val dayDateFormatter = SimpleDateFormat(dayPattern, Locale.US)
    private val timeDateFormatter = SimpleDateFormat(timePattern, Locale.US)
    override fun format(date: Long): String =
        dayDateFormatter.format(Date(date.toMilliSeconds()))

    override fun formatTime(date: Long): String =
        timeDateFormatter.format(Date(date.toMilliSeconds()))

    private fun Long.toMilliSeconds() = this * 1000L
}
