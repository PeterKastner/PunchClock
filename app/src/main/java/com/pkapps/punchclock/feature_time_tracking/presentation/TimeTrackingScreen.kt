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
import androidx.compose.material3.SnackbarResult.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.pkapps.punchclock.R
import com.pkapps.punchclock.core.util.inHoursMinutes
import com.pkapps.punchclock.feature_time_tracking.presentation.TimeTrackingEvent.*
import com.pkapps.punchclock.feature_time_tracking.presentation.components.HeaderItem
import com.pkapps.punchclock.feature_time_tracking.presentation.components.WorkTimeCard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TimeTrackingScreen(
    viewModel: TimeTrackingViewModel,
    onEvent: (TimeTrackingEvent) -> Unit
) {

    val state by viewModel.state.collectAsState(initial = TimeTrackingState())

    val workTimesToDisplay = remember(state.workTimes) {
        state.workTimes.filter { it.hasEnd() && it.hasStart() }
            .sortedByDescending { it.end }
    }

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

    val lifecycleOwner = LocalLifecycleOwner.current

    val uiEvent = remember(viewModel.uiEvent, lifecycleOwner) {
        viewModel.uiEvent.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    val actionLabel = stringResource(R.string.undo)

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        uiEvent.collectLatest { event ->
            Timber.i("uiEvent = '$event'")
            when (event) {
                is UiEvent.ShowDeletedWorkTimeMessage -> {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true,
                        actionLabel = actionLabel
                    )

                    when (snackbarResult) {
                        Dismissed -> Timber.d("Snackbar dismissed")
                        ActionPerformed -> onEvent(UndoDeleteWorkTime(event.workTime))
                    }
                }
                is UiEvent.ShowDeletedWorkTimesMessage -> {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true,
                        actionLabel = actionLabel
                    )

                    when (snackbarResult) {
                        Dismissed -> Timber.d("Snackbar dismissed")
                        ActionPerformed -> {
                            Timber.d("orders = ${event.workTimes}")
                            onEvent(UndoDeleteWorkTimes(event.workTimes))
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

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
                        onClick = { onEvent(StartTracking) },
                        enabled = !showCurrentWorkTime,
                        content = { Text(text = "Start") }
                    )

                    Button(
                        onClick = { onEvent(StopTracking) },
                        enabled = state.currentWorkTime.hasStart(),
                        content = { Text(text = "Stop") }
                    )

                    Button(
                        onClick = { onEvent(DeleteWorkTimes(workTimesToDisplay)) },
                        enabled = workTimesToDisplay.isNotEmpty(),
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
                        border = BorderStroke(width = 4.dp, color = colorScheme.secondary),
                        onDeleteClick = { onEvent(DeleteWorkTime(it)) },
                        onCommentSubmit = { newComment ->
                            Timber.i("comment = '$newComment'")
                            onEvent(UpdateWorkTime(workTime = state.currentWorkTime.copy(comment = newComment)))
                        },
                        onPauseSubmit = { newDuration ->
                            Timber.i("duration = '$newDuration'")
                            onEvent(UpdateWorkTime(workTime = state.currentWorkTime.copy(pause = newDuration)))
                        },
                        onStartTimeSubmit = { newStartTime ->
                            Timber.i("start time = '$newStartTime'")
                            onEvent(UpdateWorkTime(workTime = state.currentWorkTime.copy(start = state.currentWorkTime.start?.with(newStartTime))))
                        },
                        onEndTimeSubmit = { newEndTime ->
                            Timber.i("end time = '$newEndTime'")
                            onEvent(UpdateWorkTime(workTime = state.currentWorkTime.copy(end = state.currentWorkTime.end?.with(newEndTime))))
                        },
                        onStartDateSubmit = { newStartDate ->
                            Timber.i("start date = '$newStartDate'")
                            onEvent(UpdateWorkTime(workTime = state.currentWorkTime.copy(start = state.currentWorkTime.start?.with(newStartDate))))
                        },
                        onEndDateSubmit = { newEndDate ->
                            Timber.i("end date = '$newEndDate'")
                            onEvent(UpdateWorkTime(workTime = state.currentWorkTime.copy(end = state.currentWorkTime.end?.with(newEndDate))))
                        }
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

                workTimes.groupBy { it.start!!.toLocalDate() }.forEach { (date, workTimes) ->

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
                            WorkTimeCard(
                                workTime = it,
                                onDeleteClick = { onEvent(DeleteWorkTime(it)) },
                                onCommentSubmit = { newComment ->
                                    Timber.i("comment = '$newComment'")
                                    onEvent(UpdateWorkTime(workTime = it.copy(comment = newComment)))
                                },
                                onPauseSubmit = { newDuration ->
                                    Timber.i("duration = '$newDuration'")
                                    onEvent(UpdateWorkTime(workTime = it.copy(pause = newDuration)))
                                },
                                onStartTimeSubmit = { newStartTime ->
                                    Timber.i("start time = '$newStartTime'")
                                    onEvent(UpdateWorkTime(workTime = it.copy(start = it.start?.with(newStartTime))))
                                },
                                onEndTimeSubmit = { newEndTime ->
                                    Timber.i("end time = '$newEndTime'")
                                    onEvent(UpdateWorkTime(workTime = it.copy(end = it.end?.with(newEndTime))))
                                },
                                onStartDateSubmit = { newStartDate ->
                                    Timber.i("start date = '$newStartDate'")
                                    onEvent(UpdateWorkTime(workTime = it.copy(start = it.start?.with(newStartDate))))
                                },
                                onEndDateSubmit = { newEndDate ->
                                    Timber.i("end date = '$newEndDate'")
                                    onEvent(UpdateWorkTime(workTime = it.copy(end = it.end?.with(newEndDate))))
                                }
                            )
                        }
                    }


                }

            }
        }

    }

}