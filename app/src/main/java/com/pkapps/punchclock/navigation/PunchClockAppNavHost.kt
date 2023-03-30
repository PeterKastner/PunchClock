package com.pkapps.punchclock.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pkapps.punchclock.MainViewModel
import com.pkapps.punchclock.presentation.MainScreen

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
            val mainViewModel = hiltViewModel<MainViewModel>()

            MainScreen(viewModel = mainViewModel)
        }
    }

}