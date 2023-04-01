package com.pkapps.punchclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import com.pkapps.punchclock.core.ui.theme.PunchClockTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PunchClockTheme {

                // Calculate the window size class for the activity's current window. If the window
                // size changes, for example when the device is rotated, the value returned by
                // calculateSizeClass will also change.
                val windowSizeClass = calculateWindowSizeClass(this)
                // Perform logic on the window size class to decide whether to use a nav rail, for example.
                val largerThanMajorityOfPhonesInPortrait =
                    windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact
                Timber.d("window size larger than majority of phones in portrait = $largerThanMajorityOfPhonesInPortrait")
                // -> TBD

                PunchClockComposeApp(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
                )

            }
        }
    }
}