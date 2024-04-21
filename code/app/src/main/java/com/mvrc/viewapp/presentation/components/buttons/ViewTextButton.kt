package com.mvrc.viewapp.presentation.components.buttons

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.presentation.theme.UiConstants.MAX_OPACITY
import com.mvrc.viewapp.presentation.theme.UiConstants.MIN_OPACITY
import com.mvrc.viewapp.presentation.theme.ViewBlue

/**
 * [Composable] for a custom text button with customizable properties.
 *
 * @param modifier The modifier for styling and positioning the text button.
 * @param textContent The text content displayed on the button.
 * @param colorContainer The background color of the button.
 * @param widthBorder The width of the border around the button.
 * @param style The text style of the button.
 * @param sizeShape The size of the rounded corners of the button.
 * @param enabled Whether the button is enabled or disabled.
 * @param onClick The callback function invoked when the button is clicked.
 */
@Composable
fun ViewTextButton(
    modifier: Modifier = Modifier,
    textContent: String,
    colorContainer: Color = ViewBlue,
    widthBorder: Dp = 0.dp,
    style: TextStyle = MaterialTheme.typography.labelLarge.copy(
        color = MaterialTheme.colorScheme.tertiary,
    ),
    sizeShape: Dp = 16.r(),
    enabled: Boolean,
    onClick: () -> Unit
) {
    // Interaction source to track the button press state
    // and change on press the opacity of the button
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundAlpha = if (isPressed || !enabled) MIN_OPACITY else MAX_OPACITY

    // State to track the pressed state for fill logic
    var isPressedForFill by rememberSaveable {
        mutableStateOf(false)
    }

    Surface(
        modifier = modifier
            .height(55.rh())
            .fillMaxWidth()
            .border(
                width = widthBorder,
                color = ViewBlue.copy(alpha = backgroundAlpha),
                shape = RoundedCornerShape(size = sizeShape)
            )
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                enabled = enabled,
                onClick = {
                    isPressedForFill = !isPressedForFill
                    onClick()
                }
            )
            .alpha(backgroundAlpha),
        shape = RoundedCornerShape(size = sizeShape),
        color = colorContainer
    ) {
        Box {
            // Text content of the button
            Text(
                text = textContent,
                modifier = Modifier.align(Alignment.Center),
                style = style,
            )
        }
    }
}