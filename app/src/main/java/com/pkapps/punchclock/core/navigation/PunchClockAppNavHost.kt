package com.pkapps.punchclock.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pkapps.punchclock.feature_time_tracking.presentation.TimeTrackingScreen
import com.pkapps.punchclock.feature_time_tracking.presentation.TimeTrackingViewModel

@Composable
fun PunchClockAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screens.Main.route,
        modifier = modifier
    ) {
        composable(route = Screens.Main.route) {

            val viewModel = hiltViewModel<TimeTrackingViewModel>()
            
            TimeTrackingScreen(
                viewModel = viewModel,
                onEvent = viewModel::onEvent
            )
        }
    }

}