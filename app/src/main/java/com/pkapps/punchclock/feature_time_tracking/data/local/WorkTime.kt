package com.pkapps.punchclock.feature_time_tracking.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "work_time")
data class WorkTime(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val start: LocalDateTime = LocalDateTime.now(),
    val end: LocalDateTime? = null,
    val pause: Duration = Duration.ZERO,
    val comment: String = "",
)
