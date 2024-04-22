package com.mvrc.viewapp.presentation.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.presentation.theme.ViewBlue

/**
 * Follow button used to follow a new [ViewUser].
 *
 * @param widthButton The width of the button.
 * @param heightButton The height of the button.
 * @param style The text style of the button label.
 * @param sizeShape The size of the button's border radius, determining its shape.
 * @param enabled A flag indicating whether the button is enabled or not.
 * @param onClick Callback function to be executed when the button is tapped.
 */
@Composable
fun FollowButton(
    widthButton: Int,
    heightButton: Int,
    sizeShape: Int,
    style: TextStyle,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .width(widthButton.rw())
            .height(heightButton.rh())
            .clickable(
                indication = null,  // Disable the default ripple effect
                interactionSource = interactionSource
            ) { onClick() },
        color = if (enabled) ViewBlue else Color.White,
        border = BorderStroke(1.dp, ViewBlue),
        shape = RoundedCornerShape(sizeShape.r())
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.follow),
                style = style.copy(color = if (enabled) Color.White else style.color),
            )
        }
    }
}
