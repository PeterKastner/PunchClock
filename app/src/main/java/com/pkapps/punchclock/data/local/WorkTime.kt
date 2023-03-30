package com.pkapps.punchclock.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration
import java.time.LocalDateTime

@Entity(tableName = "work_time")
data class WorkTime(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val pause: Duration,
    val comment: String,
)
