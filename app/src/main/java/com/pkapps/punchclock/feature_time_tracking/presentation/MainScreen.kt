package com.pkapps.punchclock.feature_time_tracking.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {

    val workTimes = viewModel.workTimes

    Scaffold { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Row {
                Button(onClick = viewModel::startWorkTimeTracking) {
                    Text(text = "Start")
                }

                Button(onClick = viewModel::stopWorkTimeTracking) {
                    Text(text = "Stop")
                }

                Button(onClick = viewModel::clearWorkTimes) {
                    Text(text = "Clear")
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                workTimes.forEach {
                    Text(text = it.toString())
                }
            }

        }

    }

}