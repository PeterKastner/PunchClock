package com.pkapps.punchclock.feature_time_tracking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkapps.punchclock.feature_time_tracking.domain.WorkTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TimeTrackingViewModel @Inject constructor(
    private val workTimeRepository: WorkTimeRepository
) : ViewModel() {

    private val _workTimes = workTimeRepository.getWorkTimes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(TimeTrackingState())
    val state = combine(_state, _workTimes) { state, workTimes ->
        state.copy(workTimes = workTimes)
    }

    fun onEvent(event: TimeTrackingEvent) {
        when (event) {
            TimeTrackingEvent.StartTracking -> startWorkTimeTracking()
            TimeTrackingEvent.StopTracking -> stopWorkTimeTracking()
            TimeTrackingEvent.DeleteTrackedTimes -> clearWorkTimes()
        }
    }

    init {
        workTimeRepository.getWorkTimeWithEndTimeOfNullOrNull().onEach { workTime ->
            Timber.i("work time with end equal to null = $workTime")
            workTime?.let { currentWorkTime ->
                _state.update { it.copy(currentWorkTime = currentWorkTime) }
            }
        }.launchIn(viewModelScope)
    }

    private fun startWorkTimeTracking() = viewModelScope.launch {

        val workTime = _state.value.currentWorkTime.copy(start = LocalDateTime.now())

        _state.update { it.copy(currentWorkTime = workTime) }

        workTimeRepository.upsertWorkTime(workTime)
    }

    private fun stopWorkTimeTracking() = viewModelScope.launch {
        val workTime = _state.value.currentWorkTime.copy(end = LocalDateTime.now())

        workTimeRepository.upsertWorkTime(workTime)
    }

    private fun clearWorkTimes() = viewModelScope.launch {
        workTimeRepository.deleteAllWorkTimes()
    }

}