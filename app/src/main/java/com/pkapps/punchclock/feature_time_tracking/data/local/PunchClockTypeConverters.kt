package com.pkapps.punchclock.feature_time_tracking.data.local

import androidx.room.TypeConverter
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object PunchClockTypeConverters {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { formatter.parse(value, LocalDateTime::from) }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    @JvmStatic
    fun toDuration(value: String): Duration {
        return Duration.parse(value)
    }

    @TypeConverter
    @JvmStatic
    fun fromDuration(duration: Duration): String {
        return duration.toString()
    }

}