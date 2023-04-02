package com.pkapps.punchclock.feature_time_tracking.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun DateHeader(
    dateString: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.End,
    shape: Shape = shapes.extraSmall,
    style: TextStyle = typography.titleLarge.copy(
        color = colorScheme.primary,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        textDecoration = TextDecoration.None
    ),
    border: BorderStroke? = null,
) {

    Card(
        shape = shape,
        border = border,
        modifier = modifier,
    ) {

        Text(
            text = dateString,
            style = style,
            textAlign = textAlign,
            modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }

}