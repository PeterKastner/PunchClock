package com.pkapps.punchclock.feature_time_tracking.presentation

import com.pkapps.punchclock.feature_time_tracking.data.local.WorkTime

data class TimeTrackingState(
    val workTimes: List<WorkTime> = emptyList(),
    val currentWorkTime: WorkTime = WorkTime()
)
