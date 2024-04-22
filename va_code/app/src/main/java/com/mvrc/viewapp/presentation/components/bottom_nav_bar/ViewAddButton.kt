package com.mvrc.viewapp.presentation.components.bottom_nav_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.presentation.theme.UiConstants
import com.mvrc.viewapp.presentation.theme.ViewBlue

/**
 * An "Add" button with a circular [ViewBlue] background.
 *
 * @param modifier Modifier for customizing the layout and appearance of the button.
 * @param onClick Callback invoked when the button is clicked.
 */
@Composable
fun ViewAddButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundAlpha = if (isPressed) UiConstants.MIN_OPACITY else UiConstants.MAX_OPACITY

    Box(
        modifier = modifier.alpha(backgroundAlpha),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .background(ViewBlue, shape = CircleShape)
                .padding(20.r())
                .clickable(
                    indication = null,  // Disable the default ripple effect
                    interactionSource = interactionSource
                ) {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = null,
                modifier = Modifier
                    .height(20.rh())
            )
        }
    }
}