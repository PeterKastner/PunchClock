package com.pkapps.punchclock.feature_time_tracking.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.duration.DurationDialog
import com.maxkeppeler.sheets.duration.models.DurationConfig
import com.maxkeppeler.sheets.duration.models.DurationFormat
import com.maxkeppeler.sheets.duration.models.DurationSelection
import java.time.Duration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PauseDialog(
    duration: Duration,
    closeSelection: () -> Unit,
    onSubmit: (newDuration: Duration) -> Unit
) {

    val useCaseState = rememberUseCaseState(
        visible = true,
        onCloseRequest = { closeSelection() }
    )

    DurationDialog(
        state = useCaseState,
        header = Header.Default(
            title = "Input your break time",
            icon = IconSource(Icons.Filled.LocalCafe)
        ),
        selection = DurationSelection { newTimeInSeconds ->
            val durationToSubmit = Duration.ofSeconds(newTimeInSeconds)
            onSubmit(durationToSubmit)
        },
        config = DurationConfig(
            timeFormat = DurationFormat.HH_MM,
            currentTime = duration.seconds
        )
    )

}