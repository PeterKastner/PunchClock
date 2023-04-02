package com.pkapps.punchclock.feature_time_tracking.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pkapps.punchclock.feature_time_tracking.presentation.components.DateHeader
import com.pkapps.punchclock.feature_time_tracking.presentation.components.WorkTimeCard
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeTrackingScreen(
    state: TimeTrackingState,
    onEvent: (TimeTrackingEvent) -> Unit
) {

    val workTimesToDisplay = remember(state.workTimes) {
        state.workTimes.filter { it.hasEnd() && it.hasStart() }
            .sortedByDescending { it.end }
    }

    val groupedWorkTimes = workTimesToDisplay.groupBy { it.start!!.toLocalDate() }

    val showCurrentWorkTime = remember(
        state.currentWorkTime.start,
        state.currentWorkTime.end,
        state.currentWorkTime.id
    ) { state.currentWorkTime.hasStart() && !state.currentWorkTime.hasEnd() }

    Scaffold { innerPadding ->

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            item {
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
            }

            item {
                AnimatedVisibility(
                    visible = showCurrentWorkTime,
                    enter = fadeIn(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    WorkTimeCard(
                        workTime = state.currentWorkTime,
                        elevation = CardDefaults.outlinedCardElevation(),
                        border = BorderStroke(width = 4.dp, color = colorScheme.secondary)
                    )
                }

            }

            item { Divider() }

            groupedWorkTimes.forEach { (date, workTimes) ->

                stickyHeader {
                    DateHeader(
                        dateString = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                            .format(date).toString(),
                        border = BorderStroke(
                            width = 2.dp,
                            color = colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                items(
                    items = workTimes,
                    key = { it.id },
                    contentType = { it }
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        WorkTimeCard(workTime = it)
                    }
                }


            }

        }

    }

}