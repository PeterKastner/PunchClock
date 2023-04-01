package com.pkapps.punchclock.feature_time_tracking.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkapps.punchclock.feature_time_tracking.data.local.WorkTime
import com.pkapps.punchclock.feature_time_tracking.domain.WorkTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val workTimeRepository: WorkTimeRepository
) : ViewModel() {

    val workTimes = mutableStateListOf<WorkTime>()

    var workTime by mutableStateOf(WorkTime())

    init {
        workTimeRepository.getWorkTimes().onEach { workTimesList ->
            Timber.i("work times = $workTimesList")
            workTimes.clear()
            workTimesList.forEach {
                workTimes.add(it)
            }
        }.launchIn(viewModelScope)

        workTimeRepository.getWorkTimeWithEndTimeOfNullOrNull().onEach {
            Timber.i("work time with end equal to null = $it")
            it?.let { workTime = it }
        }.launchIn(viewModelScope)
    }

    fun startWorkTimeTracking() = viewModelScope.launch {

        workTime = workTime.copy(start = LocalDateTime.now())

        workTimeRepository.upsertWorkTime(workTime)
    }

    fun stopWorkTimeTracking() = viewModelScope.launch {
        workTime = workTime.copy(end = LocalDateTime.now())

        workTimeRepository.upsertWorkTime(workTime)
    }

    fun clearWorkTimes() = viewModelScope.launch {
        workTimeRepository.deleteAllWorkTimes()
    }

}