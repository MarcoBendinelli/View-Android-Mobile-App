package com.mvrc.viewapp.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * [Composable] for displaying a circular progress indicator.
 *
 * @param modifier The modifier for styling and positioning the circular progress indicator.
 * @param strokeWidth The stroke width of the indicator.
 */
@Composable
fun ViewCircularProgress(modifier: Modifier = Modifier, strokeWidth: Double = 3.0) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondary,
            strokeWidth = strokeWidth.dp
        )
    }
}