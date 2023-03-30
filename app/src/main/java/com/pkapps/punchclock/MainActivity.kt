package com.pkapps.punchclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pkapps.punchclock.ui.theme.PunchClockTheme
import timber.log.Timber

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
                val largerThanMajorityOfPhonesInPortrait = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact
                Timber.d("window size larger than majority of phones in portrait = $largerThanMajorityOfPhonesInPortrait")
                // -> TBD

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PunchClockTheme {
        Greeting("Android")
    }
}