package com.pkapps.punchclock.feature_time_tracking.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(
    localDate: LocalDate,
    close: () -> Unit,
    onSubmit: (newDate: LocalDate) -> Unit
) {

    val useCaseState = rememberUseCaseState(
        visible = true,
        onCloseRequest = { close() }
    )

    CalendarDialog(
        state = useCaseState,
        selection = CalendarSelection.Date(
            selectedDate = localDate
        ) { newDate ->
            onSubmit(newDate)
        },
        config = CalendarConfig(),
    )

}