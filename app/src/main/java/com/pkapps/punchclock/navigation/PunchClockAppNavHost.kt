package com.pkapps.punchclock.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pkapps.punchclock.feature_time_tracking.presentation.TimeTrackingViewModel
import com.pkapps.punchclock.feature_time_tracking.presentation.MainScreen
import com.pkapps.punchclock.feature_time_tracking.presentation.TimeTrackingState

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
            val state by viewModel.state.collectAsState(TimeTrackingState())

            MainScreen(state = state, onEvent = viewModel::onEvent)
        }
    }

}