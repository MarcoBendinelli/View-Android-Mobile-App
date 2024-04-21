package com.mvrc.viewapp.presentation.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.mvrc.viewapp.presentation.theme.UiConstants.MAX_OPACITY
import com.mvrc.viewapp.presentation.theme.UiConstants.MIN_OPACITY

/**
 * [Composable] for creating clickable text.
 *
 * @param modifier The modifier for styling and positioning the button.
 * @param text The text to be displayed.
 * @param style The style to be applied to the text.
 * @param isEnabled  Whether the button is enabled or not.
 * @param textDecoration The decoration to be applied to the text (default is None).
 * @param onClick The callback to be invoked when the text is clicked.
 */
@Composable
fun ViewClickableText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    isEnabled: Boolean = true,
    textDecoration: TextDecoration = TextDecoration.None,
    onClick: () -> Unit
) {
    // Create a mutable interaction source to track the press state
    // and collect the pressed state
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Adjust the background alpha based on the press state
    val backgroundAlpha =
        if (isEnabled) if (isPressed) MIN_OPACITY else MAX_OPACITY else MIN_OPACITY

    Text(
        text = text,
        style = style,
        textDecoration = textDecoration,
        color = style.color.copy(alpha = backgroundAlpha),
        modifier = modifier.clickable(
            indication = null,  // Disable the default ripple effect
            interactionSource = interactionSource,
            enabled = isEnabled
        ) {
            // Invoke the provided onClick callback when the text is clicked
            onClick()
        }
    )
}