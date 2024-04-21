package com.mvrc.viewapp.presentation.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.presentation.theme.UiConstants.MAX_OPACITY
import com.mvrc.viewapp.presentation.theme.UiConstants.MIN_OPACITY

/**
 * [Composable] for rendering a back icon with an interaction effect.
 *
 * @param modifier The modifier for styling and positioning the back icon.
 * @param navigateBack The callback function to navigate back.
 */
@Composable
fun BackIcon(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    // Mutable interaction source to handle button press interactions
    // and collect the pressed state
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Calculate background alpha based on the pressed state
    val backgroundAlpha = if (isPressed) MIN_OPACITY else MAX_OPACITY

    // Surface composable to create a clickable and stylized back button
    Surface(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = backgroundAlpha),
                shape = RoundedCornerShape(size = 12.r())
            )
            .size(40.r())
            .padding(8.r())
            .alpha(backgroundAlpha)
            .testTag("backButton")
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                navigateBack() // Invoke the navigateBack callback when the button is clicked
            }
    ) {
        // Image composable to display the back icon with specified styling
        Image(
            painter = painterResource(R.drawable.icon_arrow_left),
            contentDescription = "Back Icon",
            contentScale = ContentScale.FillWidth,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.secondary
            )
        )
    }
}