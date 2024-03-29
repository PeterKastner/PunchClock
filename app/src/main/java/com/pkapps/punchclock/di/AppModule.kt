package com.pkapps.punchclock.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pkapps.punchclock.feature_time_tracking.data.WorkTimeRepositoryImpl
import com.pkapps.punchclock.feature_time_tracking.data.local.PunchClockDatabase
import com.pkapps.punchclock.feature_time_tracking.domain.WorkTimeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun providePunchClockDatabase(application: Application): PunchClockDatabase {

        val queryCallback = object : RoomDatabase.QueryCallback {
            override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                Timber.d("${PunchClockDatabase.NAME} db: sqlQuery = '$sqlQuery', bindArgs = $bindArgs")
            }

        }

        return Room.databaseBuilder(
            application,
            PunchClockDatabase::class.java,
            PunchClockDatabase.NAME
        )
            .setQueryCallback(
                queryCallback = queryCallback,
                executor = Executors.newSingleThreadExecutor()
            )
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    fun provideWorkTimeDao(database: PunchClockDatabase) = database.workTimeDao

    @Provides
    @Singleton
    fun provideWorkTimeRepository(database: PunchClockDatabase): WorkTimeRepository {
        return WorkTimeRepositoryImpl(database)
    }

}