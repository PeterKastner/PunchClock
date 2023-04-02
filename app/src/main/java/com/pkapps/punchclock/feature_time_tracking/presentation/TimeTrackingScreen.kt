package com.pkapps.punchclock.feature_time_tracking.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    state: TimeTrackingState,
    onEvent: (TimeTrackingEvent) -> Unit
) {

    val workTimesToDisplay = remember(state.workTimes) { state.workTimes.filter { it.hasEnd() } }

    val showCurrentWorkTime = remember(
        state.currentWorkTime.start,
        state.currentWorkTime.end,
        state.currentWorkTime.id
    ) { state.currentWorkTime.hasStart() && !state.currentWorkTime.hasEnd() }

    Scaffold { innerPadding ->

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { onEvent(TimeTrackingEvent.StartTracking) },
                    enabled = !showCurrentWorkTime,
                    content = { Text(text = "Start") }
                )

                Button(
                    onClick = { onEvent(TimeTrackingEvent.StopTracking) },
                    enabled = state.currentWorkTime.hasStart(),
                    content = { Text(text = "Stop") }
                )

                Button(
                    onClick = { onEvent(TimeTrackingEvent.DeleteTrackedTimes) },
                    enabled = state.workTimes.isNotEmpty(),
                    content = { Text(text = "Clear") }
                )
            }

            AnimatedVisibility(
                visible = showCurrentWorkTime,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card {
                    Text(text = state.currentWorkTime.toString())
                }
            }


            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                workTimesToDisplay.forEach {
                    Text(text = it.toString())
                }
            }

        }

    }

}