package com.pkapps.punchclock.data

import com.pkapps.punchclock.data.local.PunchClockDatabase
import com.pkapps.punchclock.data.local.WorkTime
import com.pkapps.punchclock.domain.WorkTimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkTimeRepositoryImpl @Inject constructor(
    db: PunchClockDatabase
) : WorkTimeRepository {

    private val dao = db.workTimeDao

    override fun getWorkTimes(): Flow<List<WorkTime>> {
        return dao.getWorkTimes()
    }

    override suspend fun upsertWorkTime(workTime: WorkTime) {
        dao.upsertWorkTime(workTime)
    }

    override suspend fun deleteWorkTime(workTime: WorkTime) {
        dao.deleteWorkTime(workTime)
    }
}