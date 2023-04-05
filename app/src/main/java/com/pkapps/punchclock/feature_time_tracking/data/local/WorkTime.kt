package com.pkapps.punchclock.feature_time_tracking.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "work_time")
data class WorkTime(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val start: LocalDateTime? = null,
    val end: LocalDateTime? = null,
    val pause: Duration = Duration.ZERO,
    val comment: String = "",
) {
    fun hasStart() = this.start != null

    fun hasEnd() = this.end != null

    fun grossDeltaOrNull(): Duration? =
        if (hasStart() && hasEnd()) Duration.between(this.start, this.end) else null

    fun netDeltaOrNull(): Duration? = grossDeltaOrNull()?.minus(pause)

}
