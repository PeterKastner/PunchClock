package com.pkapps.punchclock.feature_time_tracking.presentation

import com.pkapps.punchclock.feature_time_tracking.data.local.WorkTime

sealed class UiEvent {
    data class ShowDeletedWorkTimeMessage(val message: String, val workTime: WorkTime) : UiEvent()
    data class ShowDeletedWorkTimesMessage(val message: String, val workTimes: List<WorkTime>) : UiEvent()
}
