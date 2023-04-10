package com.pkapps.punchclock.feature_time_tracking.presentation.components.dialogs

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDialog(
    localTime: LocalTime,
    close: () -> Unit,
    onSubmit: (newTime: LocalTime) -> Unit
) {

    val useCaseState = rememberUseCaseState(
        visible = true,
        onCloseRequest = { close() }
    )

    ClockDialog(
        state = useCaseState,
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            val timeToSubmit = LocalTime.of(hours, minutes, 0)
            onSubmit(timeToSubmit)
        },
        config = ClockConfig(
            defaultTime = localTime,
            is24HourFormat = true
        ),
    )

}