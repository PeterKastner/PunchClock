package com.pkapps.punchclock.core.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun LocalDateTime.toReadableTimeAsString(pattern: String = "E dd.MM.yyyy HH:mm"): String {

    val formatter = DateTimeFormatter.ofPattern(pattern).withLocale(Locale.getDefault())

    val truncated = this.truncatedTo(ChronoUnit.MINUTES)

    return truncated.format(formatter)
}

fun LocalDateTime.toReadableDateAsString(pattern: String = "E dd.MM.yyyy"): String {

    val formatter = DateTimeFormatter.ofPattern(pattern).withLocale(Locale.getDefault())

    val truncated = this.truncatedTo(ChronoUnit.DAYS)

    return truncated.format(formatter)
}