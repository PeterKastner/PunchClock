package com.pkapps.punchclock.feature_time_tracking.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.pkapps.punchclock.R
import com.pkapps.punchclock.core.ui.theme.PunchClockTheme
import com.pkapps.punchclock.core.util.inHoursMinutes
import com.pkapps.punchclock.core.util.toReadableTimeAsString
import com.pkapps.punchclock.feature_time_tracking.data.local.WorkTime
import de.charlex.compose.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkTimeCard(
    workTime: WorkTime,
    modifier: Modifier = Modifier,
    shape: Shape = shapes.medium,
    elevation: CardElevation = CardDefaults.cardElevation(),
    enableSwipe: Boolean = true,
    border: BorderStroke? = null,
    //onClick: (WorkTime) -> Unit = { },
    onDeleteClick: (WorkTime) -> Unit,
    onCommentSubmit: (comment: String) -> Unit,
    onPauseSubmit: (pause: Duration) -> Unit,
    onStartTimeSubmit: (start: LocalTime) -> Unit,
    onEndTimeSubmit: (end: LocalTime) -> Unit,
    onStartDateSubmit: (start: LocalDate) -> Unit,
    onEndDateSubmit: (end: LocalDate) -> Unit
) {

    val showNetDelta = remember { workTime.netDeltaOrNull() != null }

    var showCommentDialog by remember { mutableStateOf(false) }

    if (showCommentDialog) {
        CommentDialog(
            text = workTime.comment,
            closeSelection = { showCommentDialog = false },
            onSubmit = onCommentSubmit
        )
    }

    var showDurationDialog by remember { mutableStateOf(false) }

    if (showDurationDialog) {
        PauseDialog(
            duration = workTime.pause,
            closeSelection = { showDurationDialog = false },
            onSubmit = onPauseSubmit
        )
    }

    var showStartTimeDialog by remember { mutableStateOf(false) }

    if (showStartTimeDialog && workTime.hasStart()) {
        TimeDialog(
            localTime = workTime.start!!.toLocalTime(),
            close = { showStartTimeDialog = false },
            onSubmit = onStartTimeSubmit
        )
    }

    var showEndTimeDialog by remember { mutableStateOf(false) }

    if (showEndTimeDialog && workTime.hasEnd()) {
        TimeDialog(
            localTime = workTime.end!!.toLocalTime(),
            close = { showEndTimeDialog = false },
            onSubmit = onEndTimeSubmit
        )
    }

    var showStartDateDialog by remember { mutableStateOf(false) }

    if (showStartDateDialog && workTime.hasStart()) {
        DateDialog(
            localDate = workTime.start!!.toLocalDate(),
            close = { showStartDateDialog = false },
            onSubmit = onStartDateSubmit
        )
    }

    var showEndDateDialog by remember { mutableStateOf(false) }

    if (showEndDateDialog && workTime.hasEnd()) {
        DateDialog(
            localDate = workTime.end!!.toLocalDate(),
            close = { showEndDateDialog = false },
            onSubmit = onEndDateSubmit
        )
    }

    val hapticFeedback = LocalHapticFeedback.current
    fun performHapticFeedback() =
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)

    val revealState = rememberRevealState(
        confirmStateChange = {
            performHapticFeedback()
            true
        }
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // this will reset fully revealed items back to the default state
        // e.g when navigating back from another screen
        revealState.reset()
    }

    RevealSwipe(
        directions = setOf(RevealDirection.StartToEnd),
        state = revealState,
        enableSwipe = enableSwipe,
        hiddenContentStart = {
            IconButton(
                onClick = {
                    onDeleteClick(workTime)
                    scope.launch { revealState.reset() }
                    performHapticFeedback()
                }
            ) {
                Icon(
                    modifier = Modifier.padding(horizontal = 26.dp),
                    imageVector = Icons.Default.Delete,
                    tint = MaterialTheme.colors.onError,
                    contentDescription = null
                )
            }
        },
        backgroundCardStartColor = MaterialTheme.colors.error,
        backgroundStartActionLabel = stringResource(R.string.cd_delete_item),
        backgroundEndActionLabel = null,
        closeOnBackgroundClick = true,
        closeOnContentClick = true,
//        onContentClick = {
//            performHapticFeedback()
//
//            val isOpen = revealState.targetValue != RevealValue.Default
//            if (isOpen) {
//                scope.launch { revealState.reset() }
//                return@RevealSwipe
//            }
//
//            //onClick(workTime)
//        },
    ) {

        Card(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            elevation = elevation,
            border = border
        ) {

            val style = typography.titleLarge.copy(
                color = colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                textDecoration = TextDecoration.None
            )

            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                // start
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    showStartTimeDialog = true
                                    showStartDateDialog = true
                                }
                            )
                        }
                ) {

                    Text(
                        text = "Start",
                        style = style,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = workTime.start?.toReadableTimeAsString() ?: "",
                        style = style,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                // end
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    showEndTimeDialog = true
                                    showEndDateDialog = true
                                }
                            )
                        }
                ) {

                    Text(
                        text = "End",
                        style = style,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = workTime.end?.toReadableTimeAsString() ?: "",
                        style = style
                    )
                }

                // pause duration
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    showDurationDialog = true
                                }
                            )
                        }
                ) {

                    Text(
                        text = "Pause",
                        style = style
                    )
                    Text(
                        text = workTime.pause.inHoursMinutes(),
                        style = style
                    )
                }

                // net time delta
                if (showNetDelta) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    ) {

                        val color = workTime.netDeltaOrNull()?.let {
                            when {
                                it <= Duration.ofHours(8) -> Color.Green
                                it <= Duration.ofHours(9) -> Color.Green.copy(alpha = 0.6f)
                                else -> colorScheme.error
                            }
                        }

                        Text(
                            text = "Delta",
                            style = style
                        )
                        Text(
                            text = workTime.netDeltaOrNull()?.inHoursMinutes() ?: "",
                            style = style,
                            color = color ?: Color.Unspecified
                        )
                    }
                }

                // comment
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    showCommentDialog = true
                                }
                            )
                        }
                ) {

                    Text(
                        text = "Comment",
                        style = style
                    )
                    Text(
                        text = workTime.comment,
                        style = style,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "light preview", showBackground = true, showSystemUi = false)
@Preview(name = "dark preview", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WorkTimeCardPreview(
    @PreviewParameter(WorkTimeProvider::class) workTime: WorkTime
) {

    PunchClockTheme {
        Scaffold { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {

                WorkTimeCard(
                    workTime = workTime,
                    modifier = Modifier.wrapContentSize(),
                    shape = shapes.medium,
                    elevation = CardDefaults.elevatedCardElevation(),
                    border = BorderStroke(width = 2.dp, color = colorScheme.primary),
                    onDeleteClick = {},
                    onCommentSubmit = {},
                    onPauseSubmit = {},
                    onStartTimeSubmit = {},
                    onEndTimeSubmit = {},
                    onStartDateSubmit = {},
                    onEndDateSubmit = {},
                )
            }
        }
    }

}

class WorkTimeProvider : PreviewParameterProvider<WorkTime> {
    override val values = listOf(
        WorkTime(
            id = UUID.randomUUID(),
            start = LocalDateTime.now(),
            end = LocalDateTime.now() + Duration.ofHours(8).plus(Duration.ofMinutes(26)),
            pause = Duration.ofMinutes(52),
            comment = ""
        ),
    ).asSequence()
}