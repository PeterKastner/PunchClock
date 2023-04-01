package com.pkapps.punchclock.domain

import com.pkapps.punchclock.data.local.WorkTime
import kotlinx.coroutines.flow.Flow

interface WorkTimeRepository {

    fun getWorkTimes(): Flow<List<WorkTime>>

    fun getWorkTimeWithEndTimeOfNullOrNull(): Flow<WorkTime?>

    suspend fun upsertWorkTime(workTime: WorkTime)

    suspend fun upsertWorkTimes(vararg workTimes: WorkTime)

    suspend fun deleteWorkTime(workTime: WorkTime)

    suspend fun deleteAllWorkTimes()

}