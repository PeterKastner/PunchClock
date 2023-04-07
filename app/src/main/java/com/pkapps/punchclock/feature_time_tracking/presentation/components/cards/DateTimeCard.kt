package com.pkapps.punchclock.feature_time_tracking.presentation.components.cards

import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pkapps.punchclock.core.ui.theme.PunchClockTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Composable
fun DateTimeCard(
    title: String,
    modifier: Modifier = Modifier,
    headerStyle: TextStyle = typography.titleMedium.copy(
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        textDecoration = TextDecoration.None
    ),
    itemStyle: TextStyle = typography.titleLarge.copy(
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        textDecoration = TextDecoration.None
    ),
    localDateTime: LocalDateTime?,
    shape: Shape = CardDefaults.elevatedShape,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    elevation: CardElevation = CardDefaults.elevatedCardElevation(
        defaultElevation = 16.dp
    ),
    dateFormatStyle: FormatStyle = FormatStyle.FULL,
    timeFormatStyle: FormatStyle = FormatStyle.SHORT,
    locale: Locale = Locale.getDefault(),
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
) {

    val date = localDateTime?.toLocalDate()
    val dateFormat = DateTimeFormatter.ofLocalizedDate(dateFormatStyle).withLocale(locale)

    val time = localDateTime?.toLocalTime()
    val timeFormat = DateTimeFormatter.ofLocalizedTime(timeFormatStyle).withLocale(locale)

    val hapticFeedback = LocalHapticFeedback.current
    fun performHapticFeedback() =
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

    ElevatedCard(
        modifier = modifier.wrapContentSize(),
        shape = shape,
        colors = colors,
        elevation = elevation
    ) {

        Column(
            modifier = modifier.wrapContentWidth()
        ) {

            Text(
                text = title,
                style = headerStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp, start = 16.dp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                OutlinedCard(
                    shape = shapes.medium,
                    modifier = Modifier
                        //.padding(vertical = 8.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    performHapticFeedback()
                                    onDateClick()
                                }
                            )
                        }
                ) {
                    Text(
                        text = date?.format(dateFormat) ?: "",
                        style = itemStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(16.dp),
                    )
                }
                OutlinedCard(
                    shape = shapes.medium,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    performHapticFeedback()
                                    onTimeClick()
                                }
                            )
                        }
                ) {
                    Text(
                        text = time?.format(timeFormat) ?: "",
                        style = itemStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(16.dp),
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
fun DateTimeCardPreview() {

    PunchClockTheme {
        Scaffold { innerPadding ->

            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                DateTimeCard(
                    modifier = Modifier.wrapContentWidth(),
                    title = "Start",
                    localDateTime = LocalDateTime.now(),
                    dateFormatStyle = FormatStyle.LONG,
                    locale = Locale.GERMAN,
                    onDateClick = {},
                    onTimeClick = {},
                )
            }
        }
    }

}