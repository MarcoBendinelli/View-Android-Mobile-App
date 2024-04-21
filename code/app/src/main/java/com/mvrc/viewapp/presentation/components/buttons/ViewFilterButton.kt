package com.mvrc.viewapp.presentation.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold
import com.mvrc.viewapp.presentation.theme.ViewAlmostBlack
import com.mvrc.viewapp.presentation.theme.ViewBlue
import com.mvrc.viewapp.presentation.theme.ViewGray
import com.mvrc.viewapp.presentation.theme.ViewGrayBubbleButton

/**
 * [Composable] for a filter button with customizable properties.
 *
 * @param modifier The modifier for styling and positioning the filter button.
 * @param textContent The text content displayed on the button.
 * @param isOpacityBehaviour Boolean that indicates whether the button changes opacity instead of color on click.
 * @param isClicked Boolean value that is true if the button is pressed, false otherwise.
 * @param onClick The callback function invoked when the button is clicked.
 */
@Composable
fun ViewFilterButton(
    modifier: Modifier = Modifier,
    textContent: String,
    isOpacityBehaviour: Boolean,
    isClicked: Boolean,
    onClick: () -> Unit
) {
    // Interaction source to track the button press state
    // and change the style of the button accordingly
    val interactionSource = remember { MutableInteractionSource() }

    // Determine background color and text color based on pressed state
    val backgroundColor = if (!isClicked) ViewGrayBubbleButton else ViewBlue
    val textColor = if (!isClicked) ViewAlmostBlack else Color.White
    val textColorWithOpacityBehaviour = if (!isClicked) ViewGray else ViewAlmostBlack

    Surface(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = interactionSource,
            ) {
                onClick()
            },
        shape = RoundedCornerShape(size = 20.r()),
        color = if (isOpacityBehaviour) ViewGrayBubbleButton else backgroundColor
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 18.rw(), vertical = 10.rh()),
            text = textContent,
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = openSansSemiBold
            ),
            color = if (isOpacityBehaviour) textColorWithOpacityBehaviour else textColor,
        )
    }
}