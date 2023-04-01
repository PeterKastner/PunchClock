package com.pkapps.punchclock.feature_time_tracking.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    state: TimeTrackingState,
    onEvent: (TimeTrackingEvent) -> Unit
) {

    Scaffold { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Button(
                    onClick = { onEvent(TimeTrackingEvent.StartTracking) },
                    content = { Text(text = "Start") }
                )

                Button(
                    onClick = { onEvent(TimeTrackingEvent.StopTracking) },
                    content = { Text(text = "Stop") }
                )

                Button(
                    onClick = { onEvent(TimeTrackingEvent.DeleteTrackedTimes) },
                    content = { Text(text = "Clear") }
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.workTimes.forEach {
                    Text(text = it.toString())
                }
            }

        }

    }

}