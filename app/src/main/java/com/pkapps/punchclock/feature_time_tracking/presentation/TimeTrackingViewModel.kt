package com.pkapps.punchclock.feature_time_tracking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkapps.punchclock.feature_time_tracking.data.local.WorkTime
import com.pkapps.punchclock.feature_time_tracking.domain.WorkTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: TimeTrackingEvent) {
        when (event) {
            TimeTrackingEvent.StartTracking -> startWorkTimeTracking()
            TimeTrackingEvent.StopTracking -> stopWorkTimeTracking()
            is TimeTrackingEvent.UpdateWorkTime -> updateWorkTime(workTime = event.workTime)
            is TimeTrackingEvent.DeleteWorkTime -> deleteWorkTime(workTime = event.workTime)
            is TimeTrackingEvent.UndoDeleteWorkTime -> undoDeleteWorkTime(workTime = event.workTime)
            is TimeTrackingEvent.DeleteWorkTimes -> deleteWorkTimes(workTimes = event.workTimes)
            is TimeTrackingEvent.UndoDeleteWorkTimes -> undoDeleteWorkTimes(workTimes = event.workTimes)
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

        _state.update { it.copy(currentWorkTime = WorkTime()) }
    }

    private fun updateWorkTime(workTime: WorkTime) = viewModelScope.launch {
        workTimeRepository.upsertWorkTime(workTime)

        if (workTime == _state.value.currentWorkTime) _state.update { it.copy(currentWorkTime = workTime) }
    }

    private fun deleteWorkTime(workTime: WorkTime) = viewModelScope.launch {
        workTimeRepository.deleteWorkTime(workTime)

        if (workTime == _state.value.currentWorkTime) _state.update { it.copy(currentWorkTime = WorkTime()) }

        _uiEvent.send(
            UiEvent.ShowDeletedWorkTimeMessage(
                message = "Zeit gelöscht.",
                workTime = workTime
            )
        )
    }

    private fun undoDeleteWorkTime(workTime: WorkTime) = viewModelScope.launch {
        workTimeRepository.upsertWorkTime(workTime)
    }

    private fun deleteWorkTimes(workTimes: List<WorkTime>) = viewModelScope.launch {
        workTimeRepository.deleteWorkTimes(workTimes)
        _uiEvent.send(
            UiEvent.ShowDeletedWorkTimesMessage(
                message = "Alle Zeiten gelöscht.",
                workTimes = workTimes.filter { it.hasEnd() && it.hasStart() }
            )
        )
    }

    private fun undoDeleteWorkTimes(workTimes: List<WorkTime>) = viewModelScope.launch {
        workTimeRepository.upsertWorkTimes(workTimes)
    }


}