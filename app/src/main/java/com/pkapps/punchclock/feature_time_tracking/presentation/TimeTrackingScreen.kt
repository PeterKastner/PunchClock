package com.pkapps.punchclock.feature_time_tracking.presentation

import android.content.Intent
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.pkapps.punchclock.core.util.inHoursMinutes
import com.pkapps.punchclock.feature_time_tracking.presentation.components.HeaderItem
import com.pkapps.punchclock.feature_time_tracking.presentation.components.WorkTimeCard
import kotlinx.coroutines.launch
import java.io.File
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields

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

    val workTimesGroupedByStartDate = workTimesToDisplay.groupBy { it.start!!.toLocalDate() }

    val workTimesGroupedByCalendarWeek = workTimesToDisplay.groupBy {

        val weekFields = WeekFields.ISO

        it.start!!.get(weekFields.weekOfYear())
    }

    val showCurrentWorkTime = remember(
        state.currentWorkTime.start,
        state.currentWorkTime.end,
        state.currentWorkTime.id
    ) { state.currentWorkTime.hasStart() && !state.currentWorkTime.hasEnd() }

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

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

                    Button(
                        onClick = {

                            scope.launch {

                                val csvFile = File(context.filesDir, "workTimes.csv")

                                csvWriter().open(csvFile) {
                                    writeRow(listOf("id", "start", "end", "pause", "comment"))
                                    workTimesToDisplay.forEach {
                                        writeRow(
                                            it.id,
                                            it.start!!.truncatedTo(ChronoUnit.MINUTES),
                                            it.end!!.truncatedTo(ChronoUnit.MINUTES),
                                            it.pause,
                                            it.comment
                                        )
                                    }
                                }

                                val fileUri = FileProvider.getUriForFile(
                                    context,
                                    context.applicationContext.packageName + ".provider",
                                    csvFile.absoluteFile
                                )

                                val intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    type = "text/csv"
                                    putExtra(Intent.EXTRA_SUBJECT, csvFile.name)
                                    putExtra(Intent.EXTRA_STREAM, fileUri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                                }

                                val shareIntent =
                                    Intent.createChooser(intent, "Share work times to")

                                ContextCompat.startActivity(context, shareIntent, null)
                            }
                        },
                        enabled = workTimesToDisplay.isNotEmpty(),
                        content = { Text("Share") })
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

            workTimesGroupedByCalendarWeek.forEach { (week, workTimes) ->

                val sumOfNetDeltas =
                    workTimes.map { it.netDeltaOrNull() }.fold(Duration.ZERO, Duration::plus)

                stickyHeader {
                    HeaderItem(text = "Week $week\t\t${sumOfNetDeltas.inHoursMinutes()}")
                }

                workTimesGroupedByStartDate.forEach { (date, workTimes) ->

                    val fullStyleDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)

                    stickyHeader {
                        HeaderItem(
                            text = fullStyleDate.format(date).toString(),
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

}