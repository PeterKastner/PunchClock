package com.pkapps.punchclock.feature_time_tracking.presentation

import com.pkapps.punchclock.feature_time_tracking.data.local.WorkTime

sealed interface TimeTrackingEvent {
    object StartTracking : TimeTrackingEvent
    object StopTracking : TimeTrackingEvent
    data class DeleteWorkTimes(val workTimes: List<WorkTime>) : TimeTrackingEvent
    data class UndoDeleteWorkTimes(val workTimes: List<WorkTime>) : TimeTrackingEvent
    data class DeleteWorkTime(val workTime: WorkTime) : TimeTrackingEvent
    data class UndoDeleteWorkTime(val workTime: WorkTime) : TimeTrackingEvent
    data class UpdateWorkTime(val workTime: WorkTime) : TimeTrackingEvent
}