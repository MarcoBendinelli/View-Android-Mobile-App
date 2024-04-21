package com.mvrc.viewapp.presentation.components.top_app_bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.presentation.components.buttons.BackIcon
import com.mvrc.viewapp.presentation.components.buttons.ViewClickableText
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_H_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_V_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold
import com.mvrc.viewapp.presentation.theme.ViewBlue

/**
 * [Composable] for rendering a custom top bar with a back button on the left
 * or a skip button on the right.
 *
 * @param modifier The modifier for styling and positioning the top bar.
 * @param navigate The callback function to navigate back or skip.
 * @param showSkip Flag to determine whether to show the skip button (default is false).
 */
@Composable
fun BackSkipTopBar(modifier: Modifier = Modifier, navigate: () -> Unit, showSkip: Boolean = false) {
    // Row to horizontally arrange back/skip button and skip button if applicable
    Row(
        modifier = modifier
            .padding(
                top = AUTH_CONTENT_V_PADDING.rh(),
                start = AUTH_CONTENT_H_PADDING.rw(),
                end = AUTH_CONTENT_H_PADDING.rw()
            )
            .fillMaxWidth(),
        horizontalArrangement = if (showSkip) Arrangement.End else Arrangement.Start
    ) {
        // Display skip button if showSkip is true
        if (showSkip) {
            ViewClickableText(
                modifier = Modifier.testTag("skipButton"),
                text = stringResource(id = R.string.skip),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = openSansSemiBold,
                    color = ViewBlue
                ),
            ) {
                navigate() // Invoke the navigate callback when skip button is clicked
            }
        } else {
            // Display back button if showSkip is false
            BackIcon {
                navigate() // Invoke the navigate callback when back button is clicked
            }
        }
    }
}