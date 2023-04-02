package com.pkapps.punchclock.core.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun LocalDateTime.toReadableTimeAsString(): String {
    val pattern = "dd.MM.yyy HH:mm"
    val formatter = DateTimeFormatter.ofPattern(pattern)

    val truncated = this.truncatedTo(ChronoUnit.MINUTES)

    return truncated.format(formatter)
}