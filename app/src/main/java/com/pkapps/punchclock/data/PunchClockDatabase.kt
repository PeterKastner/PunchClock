package com.pkapps.punchclock.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [WorkTime::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PunchClockTypeConverters::class)
abstract class PunchClockDatabase : RoomDatabase() {

    abstract val workTimeDao: WorkTimeDao

    companion object {

        const val NAME = "punch_clock_db"

    }

}