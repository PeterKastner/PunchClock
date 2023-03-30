package com.pkapps.punchclock.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkTimeDao {

    @Query("SELECT * FROM work_time")
    fun getWorkTimes(): Flow<List<WorkTime>>

    @Upsert
    suspend fun upsertWorkTime(workTime: WorkTime)

    @Delete
    suspend fun deleteWorkTime(workTime: WorkTime)

    @Query("DELETE FROM work_time")
    suspend fun deleteAllWorkTimes()

}