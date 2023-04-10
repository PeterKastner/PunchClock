package com.pkapps.punchclock.feature_time_tracking.presentation.components.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.input.InputDialog
import com.maxkeppeler.sheets.input.models.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentDialog(
    text: String = "",
    closeSelection: () -> Unit,
    onSubmit: (newComment: String) -> Unit
) {

    val inputOptions = listOf(
        InputTextField(
            type = InputTextFieldType.OUTLINED,
            header = InputHeader(
                title = "Any comments?",
                icon = IconSource(Icons.Filled.Notes)
            ),
            text = text,
//            validationListener = { value ->
//                if (value.isNullOrBlank()) ValidationResult.Invalid("Comment must not be blank.")
//                else ValidationResult.Valid
//            },
            required = false,
            key = "Input"
        )
    )

    val useCaseState = rememberUseCaseState(
        visible = true,
        onCloseRequest = { closeSelection() }
    )

    InputDialog(
        state = useCaseState,
        selection = InputSelection(
            input = inputOptions,
            onPositiveClick = { result ->
                val resultString = result.getString("Input")
                onSubmit(resultString ?: "")
            }
        )
    )

}