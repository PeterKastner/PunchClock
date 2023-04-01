package com.pkapps.punchclock.feature_time_tracking.presentation

sealed interface TimeTrackingEvent {
    object StartTracking : TimeTrackingEvent
    object StopTracking : TimeTrackingEvent
    object DeleteTrackedTimes : TimeTrackingEvent
}