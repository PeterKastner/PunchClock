package com.pkapps.punchclock.core.util

import java.time.Duration

fun Duration.inHoursMinutes(format: String = "%01d:%02d") : String {

    val hours = this.toHours()
    val minutes = this.toMinutesPart()

    return String.format(format, hours, minutes)

}