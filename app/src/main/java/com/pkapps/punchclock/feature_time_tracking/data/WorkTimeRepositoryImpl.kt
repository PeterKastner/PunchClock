package com.pkapps.punchclock.feature_time_tracking.data

import com.pkapps.punchclock.feature_time_tracking.data.local.PunchClockDatabase
import com.pkapps.punchclock.feature_time_tracking.data.local.WorkTime
import com.pkapps.punchclock.feature_time_tracking.domain.WorkTimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkTimeRepositoryImpl @Inject constructor(
    db: PunchClockDatabase
) : WorkTimeRepository {

    private val dao = db.workTimeDao

    override fun getWorkTimes(): Flow<List<WorkTime>> {
        return dao.getWorkTimes()
    }

    override fun getWorkTimeWithEndTimeOfNullOrNull(): Flow<WorkTime?> {
        return dao.getWorkTimeWithEndTimeOfNull()
    }

    override suspend fun upsertWorkTime(workTime: WorkTime) {
        dao.upsertWorkTime(workTime)
    }

    override suspend fun upsertWorkTimes(workTimes: List<WorkTime>) {
        dao.upsertWorkTimes(*workTimes.toTypedArray())
    }

    override suspend fun deleteWorkTime(workTime: WorkTime) {
        dao.deleteWorkTime(workTime)
    }

    override suspend fun deleteWorkTimes(workTimes: List<WorkTime>) {
        dao.deleteWorkTimes(*workTimes.toTypedArray())
    }

    override suspend fun deleteAllWorkTimes() {
        dao.deleteAllWorkTimes()
    }
}