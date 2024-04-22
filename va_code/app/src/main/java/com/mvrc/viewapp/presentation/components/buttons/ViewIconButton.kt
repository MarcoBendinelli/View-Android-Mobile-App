package com.mvrc.viewapp.presentation.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.presentation.theme.UiConstants.MAX_OPACITY
import com.mvrc.viewapp.presentation.theme.UiConstants.MIN_OPACITY

/**
 * [Composable] for a custom icon button with customizable properties.
 *
 * @param modifier The modifier for styling and positioning the icon button.
 * @param size The size of the icon button.
 * @param iconId The resource ID of the icon to be displayed on the button.
 * @param onClick The callback function invoked when the button is clicked.
 */
@Composable
fun ViewIconButton(
    modifier: Modifier = Modifier,
    size: Int,
    iconId: Int,
    onClick: () -> Unit
) {
    // Interaction source to track the button press state and
    // change the opacity accordingly
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundAlpha = if (isPressed) MIN_OPACITY else MAX_OPACITY

    Image(
        modifier = modifier
            .size(size.r())
            .alpha(backgroundAlpha)
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                onClick()
            },
        painter = painterResource(iconId),
        contentDescription = "Icon Button",
        contentScale = ContentScale.FillHeight,
        colorFilter = ColorFilter.tint(
            color = MaterialTheme.colorScheme.secondary
        )
    )
}