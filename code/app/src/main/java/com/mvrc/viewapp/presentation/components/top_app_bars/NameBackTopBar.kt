package com.mvrc.viewapp.presentation.components.top_app_bars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvrc.viewapp.presentation.components.buttons.BackIcon
import com.mvrc.viewapp.presentation.theme.UiConstants

/**
 * A top bar with a title and an optional Back Icon.
 *
 * @param title The text to display in the top bar.
 * @param navigateBack Callback to navigate back to the previous screen.
 * @param showBackIcon Whether the back icon should be shown or not.
 */
@Composable
fun NameBackTopBar(title: String, showBackIcon: Boolean, navigateBack: () -> Unit = {}) {

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        if (showBackIcon)
            BackIcon {
                navigateBack()
            }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title, style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = UiConstants.openSansSemiBold
                )
            )
        }
    }
}