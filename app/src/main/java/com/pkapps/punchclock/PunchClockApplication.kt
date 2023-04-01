package com.pkapps.punchclock

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PunchClockApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize timber logging library when in debug mode
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                // log method name, line number and display it in logcat as a hyperlink
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
                }
            })
        }

    }
}