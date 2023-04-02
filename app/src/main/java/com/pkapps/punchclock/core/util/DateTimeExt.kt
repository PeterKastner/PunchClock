package com.pkapps.punchclock.core.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun LocalDateTime.toReadableTimeAsString(): String {
    val pattern = "E dd.MM.yyy HH:mm"
    val formatter = DateTimeFormatter.ofPattern(pattern).withLocale(Locale.getDefault())

    val truncated = this.truncatedTo(ChronoUnit.MINUTES)

    return truncated.format(formatter)
}