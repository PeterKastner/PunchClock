package com.pkapps.punchclock

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkapps.punchclock.data.local.WorkTime
import com.pkapps.punchclock.domain.WorkTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val workTimeRepository: WorkTimeRepository
) : ViewModel() {

    val workTimes = mutableStateListOf<WorkTime>()

    init {
        workTimeRepository.getWorkTimes().onEach { workTimesList ->
            Timber.d("work times = $workTimesList")
            workTimes.clear()
            workTimesList.forEach {
                workTimes.add(it)
            }
        }.launchIn(viewModelScope)
    }

    fun startWorkTimeTracking() {

        val workTime = WorkTime(
            start = LocalDateTime.now(),
            end = LocalDateTime.MAX,
            pause = Duration.ZERO,
            comment = ""
        )

        viewModelScope.launch {
            workTimeRepository.upsertWorkTime(workTime = workTime)
        }
    }

    fun stopWorkTimeTracking() {

    }

    fun clearWorkTimes() = viewModelScope.launch {
        workTimeRepository.deleteAllWorkTimes()
    }

}