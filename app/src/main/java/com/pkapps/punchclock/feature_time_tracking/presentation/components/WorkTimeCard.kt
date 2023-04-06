package com.pkapps.punchclock.feature_time_tracking.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.pkapps.punchclock.core.ui.theme.PunchClockTheme
import com.pkapps.punchclock.core.util.inHoursMinutes
import com.pkapps.punchclock.core.util.toReadableTimeAsString
import com.pkapps.punchclock.feature_time_tracking.data.local.WorkTime
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Composable
fun WorkTimeCard(
    workTime: WorkTime,
    modifier: Modifier = Modifier,
    shape: Shape = shapes.medium,
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
) {

    val showGrossDelta = remember { workTime.grossDeltaOrNull() != null }
    val showPause = remember { workTime.pause > Duration.ZERO }
    val showNetDelta = remember { workTime.netDeltaOrNull() != null }

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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
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

            if (showGrossDelta) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {

                    Text(
                        text = "Gross",
                        style = style
                    )
                    Text(
                        text = workTime.grossDeltaOrNull()?.inHoursMinutes() ?: "",
                        style = style
                    )
                }
            }

            if (showPause) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
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
            }

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
                        text = "Net",
                        style = style
                    )
                    Text(
                        text = workTime.netDeltaOrNull()?.inHoursMinutes() ?: "",
                        style = style,
                        color = color ?: Color.Unspecified
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
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