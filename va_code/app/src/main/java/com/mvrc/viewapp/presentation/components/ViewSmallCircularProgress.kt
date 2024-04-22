package com.mvrc.viewapp.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvrc.viewapp.core.ScreenUtils.Companion.r

/**
 * A smaller [ViewCircularProgress] that displays a small circular progress indicator.
 */
@Composable
fun ViewSmallCircularProgress() {
    Box(
        modifier = Modifier
            .height(24.r())
            .width(24.r()), contentAlignment = Alignment.Center
    ) {
        ViewCircularProgress(strokeWidth = 1.5)
    }
}