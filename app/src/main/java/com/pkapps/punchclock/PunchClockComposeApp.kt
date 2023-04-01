package com.pkapps.punchclock

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.pkapps.punchclock.core.navigation.PunchClockAppNavHost
import com.pkapps.punchclock.core.ui.theme.PunchClockTheme

@Composable
fun PunchClockComposeApp(
    modifier: Modifier = Modifier
) {

    PunchClockTheme {

        val navController = rememberNavController()

        Surface {

            PunchClockAppNavHost(
                navController = navController,
                modifier = modifier.windowInsetsPadding(WindowInsets.displayCutout)
            )

        }

    }

}